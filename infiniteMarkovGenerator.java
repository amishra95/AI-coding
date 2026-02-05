/** 
//The Challenge: The "Infinite Monkey" Text GeneratorBuild a first-order Markov Chain that analyzes a body of text (like a book) and generates new sentences based on word transition probabilities.
//The Logic: Create a dictionary (frequency map) where each key is a word, and the value is a list of words that follow it in the source text.
//The Goal: Generate a 50-word paragraph that sounds syntactically plausible but semantically chaotic.
// Key Concept: Transition Matrix $P$, where $P_{ij} = P(X_{t+1} = j \mid X_t = i)$.
*/

import java.util.*;
import java.io.*;

public class MarkovTextGenerator {
    
    // The transition map: word -> list of words that follow it
    private HashMap<String, ArrayList<String>> transitionMap;
    private Random random;
    
    public MarkovTextGenerator() {
        this.transitionMap = new HashMap<>();
        this.random = new Random();
    }
    
    /**
     * Phase 1: Training - Build the model from source text
     * @param text The source text to analyze
     */
    public void train(String text) {
     
     String[] splited = text.split("\\s+");
     
     for(int i = 0; i < splited.length-1; i++){
         String currentWord = splited[i];
         String nextWord = splited[i+1];
         
         if(!this.transitionMap.containsKey(currentWord)){
             this.transitionMap.put(currentWord, new ArrayList<>());
         }
         this.transitionMap.get(currentWord).add(nextWord);
     }
     

        
        
    }
    
    /**
     * Phase 2: Generation - Generate text using the trained model
     * @param startWord The word to start generation with
     * @param length Number of words to generate
     * @return Generated text
     */
    public String generate(String startWord, int length) {
       StringBuilder sb = new StringBuilder();
       String currentWord = startWord;
       sb.append(currentWord);
       
      for(int i = 1; i <length; i++){
          String nextWord = getRandomFollower(currentWord);
            if(nextWord == null){
                break;
            }
            sb.append(" ").append(nextWord);
            currentWord = nextWord;
      }
       
        return sb.toString();
    }
    
    /**
     * Helper: Pick a random word from the list
     */
    private String getRandomFollower(String word) {
        ArrayList<String> followers = transitionMap.get(word);
        if (followers == null || followers.isEmpty()) {
            return null;
        }
        int index = random.nextInt(followers.size());
        return followers.get(index);
    }
    
    /**
     * Helper: Get a random starting word from the model
     */
    public String getRandomStartWord() {
        if (transitionMap.isEmpty()) {
            return null;
        }
        List<String> words = new ArrayList<>(transitionMap.keySet());
        return words.get(random.nextInt(words.size()));
    }
    
    /**
     * Display the transition map (for debugging)
     */
    public void printModel() {
        for (String word : transitionMap.keySet()) {
            System.out.println(word + " -> " + transitionMap.get(word));
        }
    }
    
    // Main method for testing
    public static void main(String[] args) {
        MarkovTextGenerator generator = new MarkovTextGenerator();
        
        // Sample text for testing
        String sampleText = "the cat sat on the mat. the cat ran on the floor.";
        
        // Train the model
        generator.train(sampleText);
        
        // Print the model (optional - for debugging)
        System.out.println("Transition Map:");
        generator.printModel();
        System.out.println();
        
        // Generate text
        String generated = generator.generate("the", 10);
        System.out.println("Generated text: " + generated);
    }
}
