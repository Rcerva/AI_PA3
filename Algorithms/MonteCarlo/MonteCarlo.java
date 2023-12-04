package Algorithms.MonteCarlo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import Algorithms.MonteCarlo.MonteCarlo;
import MDP.GenerateMDP;

public class MonteCarlo {
  private GenerateMDP MDP;
  private Map<String, Double> stateValues;
  private Map<String, Integer> stateVisits;
  private int numEpisodes;
  private double alpha;

  public MonteCarlo(){
    this.MDP = new GenerateMDP();
    this.stateValues = MDP.getTheStateValues();
    this.stateVisits = MDP.getStateVisits();
    this.numEpisodes = 50;
    this.alpha = 0.1;
  }

  public void simulate(){
    Random random = new Random();
    for (int episode = 0; episode < numEpisodes; episode++) {
      List<String> epiStates = new ArrayList<>();
      List<String> epiActions = new ArrayList<>();
      List<Integer> epiRewards = new ArrayList<>();

      // Initialize the starting state
      String currState = "RU8p";

      while (!currState.equals("11a")) {
        // Choose action randomly
        List<String> availActions = new ArrayList<>(MDP.getTheTransitions().get(currState).keySet());
        String action = availActions.get(random.nextInt(availActions.size()));

        // Record the state, action, and reward
        epiStates.add(currState);
        epiActions.add(action);
        MDP.GenerateMDP.TransitionTuple transition = MDP.getTheTransitions().get(currState).get(action);
        epiRewards.add(transition.getReward());

        // Update current state
        currState = transition.getNextState();
      }

      // Perform first-visit Monte Carlo update
      Set<String> visitedStates = new HashSet<>();
      for (int t = 0; t < epiStates.size(); t++) {
        String state = epiStates.get(t);

        // Check if the current state is being visited for the first time in the episode
        if (!visitedStates.contains(state)) {
          // Mark state as visited
          visitedStates.add(state);

          // Calculate the return (G_t) from the current time step to the end of the episode
          double G_t = 0.0;
          List<Integer> remainingRewards = epiRewards.subList(t, epiRewards.size());

          for (int reward : remainingRewards) {
            G_t += reward;
          }

          // Update state value using the Monte Carlo update formula
          // V(S_t) = V(S_t) + alpha * (G_t - V(S_t))
          stateValues.put(state, stateValues.get(state) + alpha * (G_t - stateValues.get(state)));

          // Incre. the visit count for the state
          stateVisits.put(state, stateVisits.get(state) + 1);
        }
      }

      // Print the states, actions, and rewards for the episode
      System.out.println("Episode " + (episode + 1) + ":");
      System.out.println("States: " + epiStates);
      System.out.println("Actions: " + epiActions);
      System.out.println("Rewards: " + epiRewards);
      System.out.println("Sum of Rewards: " + epiRewards.stream().mapToInt(Integer::intValue).sum() + "\n");
    }

    // Print the values of all states and avg reward for each episode
    System.out.println("Values of States:");
    for (String state : MDP.getStates()) {
      double avgReward = stateVisits.get(state) != 0 ? stateValues.get(state) / stateVisits.get(state) : 0;
      System.out.println(state + ": " + stateValues.get(state) + ", Average Reward: " + avgReward);
    }
  }
}
