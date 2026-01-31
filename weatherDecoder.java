//The Problem: The Antarctic Station SignalYou are a software engineer at a remote Antarctic research station. You have two tasks to complete in Java.
//Part 1: The Markov Climate ModelThe climate in Antarctica transitions between three states: SUNNY, CLOUDY, and BLIZZARD. Based on historical data, the transition matrix $A$ is:From \ ToSUNNYCLOUDYBLIZZARDSUNNY0.70.20.1CLOUDY0.30.40.3BLIZZARD0.20.30.5Task: Write a method to simulate the weather for $N$ days starting from SUNNY. This represents a pure Markov Chain.Part 2: The Hidden Markov SensorThe external weather
//station is buried in snow, so you cannot see the weather directly. You only receive a signal from a "Heat Sensor" that reports one of two values: HOT or COLD.If it's SUNNY, the sensor reports HOT 90% of the time.If it's CLOUDY, the sensor reports HOT 40% of the time.If it's BLIZZARD, the sensor reports HOT only 5% of the time.The Challenge: You receive a sequence of sensor readings: [HOT, HOT, COLD, COLD, COLD]. Use the Viterbi Algorithm to find the most likely sequence of actual weather states (SUNNY, CLOUDY, BLIZZARD) that occurred.


import java.util.Arrays;

public class WeatherDecoder {

    public static void main(String[] args) {
        // Definitions
        String[] states = {"SUNNY", "CLOUDY", "BLIZZARD"};
        
        // Observations: 0=HOT, 1=COLD
        // Sequence: [HOT, HOT, COLD, COLD, COLD]
        int[] obs = {0, 0, 1, 1, 1}; 

        double[] startProbs = {0.7, 0.2, 0.1};
        
        double[][] transProbs = {
            {0.7, 0.2, 0.1}, // From SUNNY
            {0.3, 0.4, 0.3}, // From CLOUDY
            {0.2, 0.3, 0.5}  // From BLIZZARD
        };
        
        double[][] emissionProbs = {
            {0.9, 0.1},  // SUNNY: [HOT, COLD]
            {0.4, 0.6},  // CLOUDY: [HOT, COLD]
            {0.05, 0.95} // BLIZZARD: [HOT, COLD]
        };

        String[] result = viterbi(states, obs, startProbs, transProbs, emissionProbs);
        
        System.out.println("Most likely weather sequence:");
        System.out.println(Arrays.toString(result));
    }

    public static String[] viterbi(String[] states, int[] obs, double[] start, double[][] T, double[][] E) {
        int N = states.length;
        int T_len = obs.length;

        // dp[i][t] = max probability of being in state i at time t
        double[][] dp = new double[N][T_len];
        // ptr[i][t] = index of previous state that led to this max probability
        int[][] ptr = new int[N][T_len];

        // 1. Initialization (Time step 0)
        for (int s = 0; s < N; s++) {
            dp[s][0] = start[s] * E[s][obs[0]];
        }

        // 2. Recursion (Filling the Trellis)
        for (int t = 1; t < T_len; t++) {
            for (int s = 0; s < N; s++) {
                double maxProb = -1.0;
                int bestPrevState = -1;

                for (int prevS = 0; prevS < N; prevS++) {
                    double prob = dp[prevS][t - 1] * T[prevS][s] * E[s][obs[t]];
                    if (prob > maxProb) {
                        maxProb = prob;
                        bestPrevState = prevS;
                    }
                }
                dp[s][t] = maxProb;
                ptr[s][t] = bestPrevState;
            }
        }

        // 3. Backtracking (Finding the path)
        String[] path = new String[T_len];
        double lastMaxProb = -1.0;
        int lastState = -1;

        // Find the best ending state
        for (int s = 0; s < N; s++) {
            if (dp[s][T_len - 1] > lastMaxProb) {
                lastMaxProb = dp[s][T_len - 1];
                lastState = s;
            }
        }

        // Trace backwards using the pointers
        for (int t = T_len - 1; t >= 0; t--) {
            path[t] = states[lastState];
            lastState = ptr[lastState][t];
        }

        return path;
    }
}
