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
    
    // Initial probabilities: P(starting in each state)
    private double[] initial;
    
    // Transition probabilities: P(state_j | state_i)
    // transition[i][j] = probability of going from state i to state j
    private double[][] transition;
    
    // Emission probabilities: P(observation | state)
    // emission[state][observation] = probability of seeing observation given state
    private double[][] emission;
    
    /**
     * Constructor: Initialize the HMM with probabilities
     */
    public HMM() {
        // TODO: Initialize the three probability matrices
        initial = new double[NUM_STATES];
        transition = new double[NUM_STATES][NUM_STATES];
        emission = new double[NUM_STATES][NUM_OBSERVATIONS];
        
        // TODO: Set initial probabilities
        // Example: Start with fair die 50% of the time
        initial[FAIR] = 0.5;
        initial[LOADED] = 0.5;
        
        // TODO: Set transition probabilities
        // Example: 95% chance of staying in same state, 5% chance of switching
        transition[FAIR][FAIR]= 0.95;
        transition[FAIR][LOADED] = 0.05;
        transition[LOADED][FAIR] = 0.05;
        transition[LOADED][LOADED] = 0.95;
        // TODO: Set emission probabilities
        // Fair die: each face has 1/6 probability
        // Loaded die: face 6 has 0.5 probability, others share remaining 0.5
        for(int i = 0; i < NUM_OBSERVATIONS; i++){
            emission[FAIR][i] = 1.0/6.0;
        }
        
        emission[LOADED][0]= 0.16;
        emission[LOADED][1]= 0.16;
        emission[LOADED][2]= 0.16;
        emission[LOADED][3]= 0.16;
        emission[LOADED][4] = 0.16;
        emission[LOADED][5] = 0.5;  



        
    }
    
    /**
     * Viterbi Algorithm: Find most likely sequence of hidden states
     * @param observations Array of dice rolls (values 1-6)
     * @return Array of states (FAIR or LOADED) for each observation
     */
    public int[] viterbi(int[] observations) {
        int n = observations.length;
        
        // TODO: Create viterbi matrix to store max probabilities
        // viterbi[t][state] = max probability of path ending in state at time t
        double[][] viterbi = new double[n][NUM_STATES];
        
        // TODO: Create backpointer matrix to store best previous state
        // backpointer[t][state] = which state at time t-1 led to this state at time t
        int[][] backpointer = new int[n][NUM_STATES];
    
    public int[] viterbi(int[] observations) {
    int n = observations.length;
    
    double[][] viterbi = new double[n][NUM_STATES];
    int[][] backpointer = new int[n][NUM_STATES];
    
    // Step 1 - Initialization (t = 0)
    for (int state = 0; state < NUM_STATES; state++) {
        int obsIndex = observationToIndex(observations[0]);
        viterbi[0][state] = initial[state] * emission[state][obsIndex];
        backpointer[0][state] = 0;
    }
        
        // TODO: Step 2 - Recursion (t = 1 to n-1)
        // For each time step t:
        //   For each current state j:
        //     For each previous state i:
        //       Calculate: viterbi[t-1][i] * transition[i][j] * emission[j][obs[t]]
        //     Store the maximum probability and which state it came from
        
        
        // TODO: Step 3 - Termination
        // Find the state with highest probability at the last time step
        
        
        // TODO: Step 4 - Backtracking
        // Follow backpointers from end to start to reconstruct the path
        int[] path = new int[n];
        
        
        return path;
    }
    
    /**
     * Helper method: Convert observation (1-6) to array index (0-5)
     */
    private int observationToIndex(int observation) {
        return observation - 1;
    }
    
    /**
     * Helper method: Print the probability matrices for debugging
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
            System.out.println("  Roll " + (i+1) + ": " + emission[FAIR][i]);
        }
        System.out.println("Loaded die:");
        for (int i = 0; i < NUM_OBSERVATIONS; i++) {
            System.out.println("  Roll " + (i+1) + ": " + emission[LOADED][i]);
        }
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        HMM hmm = new HMM();
        
        // Print the matrices to verify setup
        hmm.printMatrices();
        
        // Test sequence: lots of 6s suggests loaded die
        int[] observations = {6, 6, 6, 6, 1, 2, 3, 6, 6, 6};
        
        System.out.println("\nObservations:");
        for (int obs : observations) {
            System.out.print(obs + " ");
        }
        System.out.println();
        
        // Run Viterbi algorithm
        int[] predictedStates = hmm.viterbi(observations);
        
        System.out.println("\nPredicted States:");
        for (int state : predictedStates) {
            System.out.print((state == FAIR ? "F" : "L") + " ");
        }
        System.out.println();
    }
}
