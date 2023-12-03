package Algorithms.QLearning;
import java.util.*;
import MDP.GenerateMDP;
public class QLearning {

   /*  public static void main(String[] args) {
        GenerateMDP mdp = new GenerateMDP();
        int numEpisodes = 1000;
        double alpha = 0.2;
        double gamma = 0.99;
        double epsilon = 0.1;

        runQLearning(mdp, numEpisodes, alpha, gamma, epsilon);
    }*/
    public QLearning(){

    }
    public static void runQLearning(GenerateMDP mdp, int numEpisodes, double alpha, double gamma, double epsilon) {
            Map<String, Double> finalQValues = new HashMap<>();
        for (int episode = 1; episode <= numEpisodes; episode++) {
            double maxQChange = 0.0;
            String currentState = mdp.getStartState();
            double currentAlpha = alpha;

            while (!mdp.isTerminalState(currentState)) {
                // Choose action using epsilon-greedy policy
                List<String> possibleActions = Arrays.asList(mdp.getActions());
                String selectedAction = epsilonGreedyPolicy(currentState, possibleActions, mdp, epsilon);

                // Perform action, observe next state and reward
                GenerateMDP.TransitionTuple transition = mdp.getTransition(currentState, selectedAction);
                String nextState = transition.getNextState();
                double immediateReward = transition.getReward();

                // Update Q-value using Q-learning update rule
                double currentQValue = mdp.getQValue(currentState, selectedAction);
                double maxNextQValue = mdp.getMaxQValue(nextState);
                double newQValue = currentQValue + currentAlpha * (immediateReward + gamma * maxNextQValue - currentQValue);

                System.out.println("Episode " + episode + ", State: " + currentState + ", Action: " + selectedAction);
                System.out.println("  Previous QValue: " + currentQValue);
                System.out.println("  Immediate Reward: " + immediateReward);
                System.out.println("  New QValue: " + newQValue);

                finalQValues.put(getStateActionPair(currentState, selectedAction), newQValue);

                maxQChange = Math.max(maxQChange, Math.abs(newQValue - currentQValue));
                 if(maxQChange<.001){
                    printFinalQValuesAndOptimalPolicy(mdp,finalQValues);
                    return;
                }
                currentState = nextState;
            }

            // Print information after each episode
            System.out.println("Episode " + episode + ": Max Q-Value Change = " + maxQChange);

            // Decrease alpha
            currentAlpha *= 0.995;
            alpha = Math.max(0.001, currentAlpha);
        }

        // Print final Q-values and optimal policy
        printFinalQValuesAndOptimalPolicy(mdp,finalQValues);
    }

    private static String getStateActionPair(String currentState, String selectedAction) {
        return currentState + "_" + selectedAction;
    }

    private static String epsilonGreedyPolicy(String state, List<String> possibleActions, GenerateMDP mdp, double epsilon) {
        // Explore with probability epsilon, otherwise exploit
        if (Math.random() < epsilon) {
            // Explore: Randomly choose an action
            int randomIndex = (int) (Math.random() * possibleActions.size());
            return possibleActions.get(randomIndex);
        } else {
            double maxQValue = Double.NEGATIVE_INFINITY;
            String bestAction = null;
            for (String action : possibleActions) {
                double qValue = mdp.getQValue(state, action);
                if (qValue > maxQValue) {
                    maxQValue = qValue;
                    bestAction = action;
                }
            }
            return bestAction;
        }
    }

    public static void printFinalQValuesAndOptimalPolicy(GenerateMDP mdp, Map<String, Double> finalQValues) {
       // Print final Q-Values
    System.out.println("Final Q-Values:");
    Map<String, Double> stateValues = mdp.getStateValues();
    for (Map.Entry<String, Double> entry : stateValues.entrySet()) {
        String state = entry.getKey();
        System.out.println("State: " + state);
        for (String action : mdp.getActions()) {
            double qValue = finalQValues.getOrDefault(getStateActionPair(state, action), 0.0);
            System.out.println("  Q(" + state + ", " + action + ") = " + qValue);
        }
    }

        // Print optimal policy
        System.out.println("Optimal Policy:");
        for (Map.Entry<String, Double> entry : stateValues.entrySet()) {
            String state = entry.getKey();
            String optimalAction = getOptimalAction(mdp, state);
            System.out.println("Optimal action for state " + state + ": " + optimalAction);
        }
    }

    private static String getOptimalAction(GenerateMDP mdp, String state) {
        List<String> possibleActions = Arrays.asList(mdp.getActions());
        double maxQValue = Double.NEGATIVE_INFINITY;
        List<String> optimalActions = new ArrayList<>();
    
        for (String action : possibleActions) {
            double qValue = mdp.getQValue(state, action);
            if (qValue > maxQValue) {
                maxQValue = qValue;
                optimalActions.clear();
                optimalActions.add(action);
            } else if (qValue == maxQValue) {
                optimalActions.add(action);
            }
        }
    
        if (!optimalActions.isEmpty()) {
            int randomIndex = (int) (Math.random() * optimalActions.size());
            return optimalActions.get(randomIndex);
        }
    
        // No optimal action found
        return "";
    }
    
}
