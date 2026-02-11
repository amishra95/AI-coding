/**
 * Hidden Markov Model for the Dishonest Casino Problem
 */
public class HMM {

    // Number of states (Fair = 0, Loaded = 1)
    private static final int NUM_STATES = 2;
    private static final int FAIR = 0;
    private static final int LOADED = 1;

    // Number of possible observations (dice faces 1-6)
    private static final int NUM_OBSERVATIONS = 6;

    // Initial probabilities
    private double[] initial;

    // Transition probabilities
    private double[][] transition;

    // Emission probabilities
    private double[][] emission;

    /**
     * Constructor: Initialize the HMM with probabilities
     */
    public HMM() {

        initial = new double[NUM_STATES];
        transition = new double[NUM_STATES][NUM_STATES];
        emission = new double[NUM_STATES][NUM_OBSERVATIONS];

        // Initial probabilities
        initial[FAIR] = 0.5;
        initial[LOADED] = 0.5;

        // Transition probabilities
        transition[FAIR][FAIR] = 0.95;
        transition[FAIR][LOADED] = 0.05;
        transition[LOADED][FAIR] = 0.05;
        transition[LOADED][LOADED] = 0.95;

        // Emission probabilities

        // Fair die: uniform
        for (int i = 0; i < NUM_OBSERVATIONS; i++) {
            emission[FAIR][i] = 1.0 / 6.0;
        }

        // Loaded die:
        // Face 6 has probability 0.5
        // Remaining 0.5 shared among 1–5
        for (int i = 0; i < 5; i++) {
            emission[LOADED][i] = 0.1;   // 0.5 / 5
        }
        emission[LOADED][5] = 0.5;
    }

    /**
     * Viterbi Algorithm: Find most likely sequence of hidden states
     */
    public int[] viterbi(int[] observations) {

        int n = observations.length;

        double[][] viterbi = new double[n][NUM_STATES];
        int[][] backpointer = new int[n][NUM_STATES];

        // Step 1 - Initialization (t = 0)
        int firstObsIndex = observationToIndex(observations[0]);

        for (int state = 0; state < NUM_STATES; state++) {
            viterbi[0][state] = initial[state] * emission[state][firstObsIndex];
            backpointer[0][state] = 0;
        }

        // Step 2 - Recursion
        for (int t = 1; t < n; t++) {

            int obsIndex = observationToIndex(observations[t]);

            for (int currState = 0; currState < NUM_STATES; currState++) {

                double maxProb = -1.0;
                int bestPrevState = 0;

                for (int prevState = 0; prevState < NUM_STATES; prevState++) {

                    double prob =
                            viterbi[t - 1][prevState]
                            * transition[prevState][currState]
                            * emission[currState][obsIndex];

                    if (prob > maxProb) {
                        maxProb = prob;
                        bestPrevState = prevState;
                    }
                }

                viterbi[t][currState] = maxProb;
                backpointer[t][currState] = bestPrevState;
            }
        }

        // Step 3 - Termination
        double maxFinalProb = -1.0;
        int bestFinalState = 0;

        for (int state = 0; state < NUM_STATES; state++) {
            if (viterbi[n - 1][state] > maxFinalProb) {
                maxFinalProb = viterbi[n - 1][state];
                bestFinalState = state;
            }
        }

        // Step 4 - Backtracking
        int[] path = new int[n];
        path[n - 1] = bestFinalState;

        for (int t = n - 2; t >= 0; t--) {
            path[t] = backpointer[t + 1][path[t + 1]];
        }

        return path;
    }

    /**
     * Convert observation (1-6) to index (0-5)
     */
    private int observationToIndex(int observation) {
        return observation - 1;
    }

    /**
     * Print matrices for debugging
     */
    public void printMatrices() {

        System.out.println("Initial Probabilities:");
        System.out.println("Fair: " + initial[FAIR] + ", Loaded: " + initial[LOADED]);
        System.out.println();

        System.out.println("Transition Probabilities:");
        System.out.println("Fair -> Fair: " + transition[FAIR][FAIR]);
        System.out.println("Fair -> Loaded: " + transition[FAIR][LOADED]);
        System.out.println("Loaded -> Fair: " + transition[LOADED][FAIR]);
        System.out.println("Loaded -> Loaded: " + transition[LOADED][LOADED]);
        System.out.println();

        System.out.println("Emission Probabilities:");
        System.out.println("Fair die:");
        for (int i = 0; i < NUM_OBSERVATIONS; i++) {
            System.out.println("  Roll " + (i + 1) + ": " + emission[FAIR][i]);
        }

        System.out.println("Loaded die:");
        for (int i = 0; i < NUM_OBSERVATIONS; i++) {
            System.out.println("  Roll " + (i + 1) + ": " + emission[LOADED][i]);
        }
    }

    /**
     * Main method for testing
     */
    public static void main(String[] args) {

        HMM hmm = new HMM();

        hmm.printMatrices();

        int[] observations = {6, 6, 6, 6, 1, 2, 3, 6, 6, 6};

        System.out.println("\nObservations:");
        for (int obs : observations) {
            System.out.print(obs + " ");
        }
        System.out.println();

        int[] predictedStates = hmm.viterbi(observations);

        System.out.println("\nPredicted States:");
        for (int state : predictedStates) {
            System.out.print((state == FAIR ? "F" : "L") + " ");
        }
        System.out.println();
    }
}
