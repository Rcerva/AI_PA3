package Algorithms.ValueIteration;
import MDP.GenerateMDP;

import java.util.*;


public class ValueIteration {
    public static void main(String[] args) {
        GenerateMDP mdp = new GenerateMDP();
        double discountFactor = 0.99;
        double epsilon = 0.001;

        // Initialize state values to 0
        Map<String, Double> stateValues = mdp.getStateValues();

        // Value Iteration
        int iteration = 0;
        while (true) {
            double maxChange = 0.0;

            // Iterate over all states
            for (String state : stateValues.keySet()) {
                if (!mdp.isTerminalState(state)) {
                    double oldValue = stateValues.get(state);
                    double bestActionValue = calculateBestActionValue(mdp, state, discountFactor);
                    stateValues.put(state, bestActionValue);

                    // Update the maximum change
                    maxChange = Math.max(maxChange, Math.abs(bestActionValue - oldValue));
                }
            }

            // Print values and check convergence
            System.out.println("Iteration: " + iteration);
            printStateValues(stateValues);
            iteration++;

            // Check for convergence
            if (maxChange < epsilon) {
                break;
            }
        }

        // Print final results
        System.out.println("Number of iterations: " + iteration);
        System.out.println("Final values:");
        printStateValues(stateValues);
        System.out.println("Optimal policy:");
        printOptimalPolicy(mdp, stateValues);
    }

    private static double calculateBestActionValue(GenerateMDP mdp, String state, double discountFactor) {
        double bestActionValue = Double.NEGATIVE_INFINITY;    
        // Iterate over all possible actions for the given state
        for (String action : mdp.getActions()) {
            // Calculate the action value for the current action
            double actionValue = calculateActionValue(mdp, state, action, discountFactor);
            
            // Update the bestActionValue with the maximum value found so far
            bestActionValue = Math.max(bestActionValue, actionValue);
        }
    
        // Return the maximum action value for the state
        return bestActionValue;
    }
    
    private static double calculateActionValue(GenerateMDP mdp, String state, String action, double discountFactor) {
        double actionValue = 0.0;    
        // Get the transition information for the current state and action
        GenerateMDP.TransitionTuple transition = mdp.getTransitions().get(state).get(action);
        if (transition != null) {
            // Calculate the action value using the Bellman equation
            actionValue = mdp.getActionProbability(state, action)
                    * (transition.getReward() + discountFactor * calculateBestActionValue(mdp, transition.getNextState(), discountFactor));
        }    
        // Return the calculated action value
        return actionValue;
    }
    
    private static void printStateValues(Map<String, Double> stateValues) {
        // Iterate over all states and print their values
        for (Map.Entry<String, Double> entry : stateValues.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println(); // Add an empty line for better readability
    }
    
    private static void printOptimalPolicy(GenerateMDP mdp, Map<String, Double> stateValues) {
        // Iterate over all states and print the Q-values and optimal actions
        for (String state : stateValues.keySet()) {
            // Check if the state is not a terminal state
            if (!mdp.isTerminalState(state)) {
                System.out.println("Q-values for state " + state + ":");
    
                // Iterate over all possible actions for the state and print their Q-values
                for (String action : mdp.getActions()) {
                    double qValue = calculateActionValue(mdp, state, action, 0.99);
                    System.out.println(action + ": " + qValue);
                }
    
                double maxActionValue = Double.NEGATIVE_INFINITY;
                List<String> optimalActions = new ArrayList<>();
    
                // Find the optimal action(s) with the maximum Q-value
                for (String action : mdp.getActions()) {
                    double actionValue = calculateActionValue(mdp, state, action, 0.99);
                    if (actionValue > maxActionValue) {
                        maxActionValue = actionValue;
                        optimalActions.clear();
                        optimalActions.add(action);
                    } else if (actionValue == maxActionValue) {
                        optimalActions.add(action);
                    }
                }    
                // Print the optimal action(s) for the state
                System.out.println("Optimal action(s) for state " + state + ": " + String.join(", ", optimalActions));
            }
        }
    }    
    
    //Not used anymore
    // private static String getOptimalAction(GenerateMDP mdp, String state) {
    //     double discountFactor = .99; // Get the discount factor from GenerateMDP
    //     List<String> possibleActions = Arrays.asList(mdp.getActions());
    //     double maxQValue = Double.NEGATIVE_INFINITY;
    //     List<String> optimalActions = new ArrayList<>();
    
    //     for (String action : possibleActions) {
    //         GenerateMDP.TransitionTuple transition = mdp.getTransitions().get(state).get(action);
    
    //         if (transition != null) {
    //             double qValue = calculateQValue(mdp, state, action, discountFactor, transition.getNextState());
    //             if (qValue > maxQValue) {
    //                 maxQValue = qValue;
    //                 optimalActions.clear();
    //                 optimalActions.add(action);
    //             } else if (qValue == maxQValue) {
    //                 optimalActions.add(action);
    //             }
    //         }
    //     }
    
    //     if (!optimalActions.isEmpty()) {
    //         int randomIndex = (int) (Math.random() * optimalActions.size());
    //         return optimalActions.get(randomIndex);
    //     }
    //     // No optimal action found
    //     return "";
    // }
    // private static double calculateQValue(GenerateMDP mdp, String state, String action, double discountFactor, String nextState) {
    //     GenerateMDP.TransitionTuple transition = mdp.getTransitions().get(state).get(action);
    
    //     if (transition != null) {
    //         return mdp.getActionProbability(state, action) * (transition.getReward() + discountFactor * mdp.getMaxQValue(nextState));
    //     } else {
    //         // If the transition is null, return a default value or handle it as appropriate for your case
    //         return 0.0; // Change this to a suitable default value
    //     }
    // }    
}
