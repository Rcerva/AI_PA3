package MDP;

import java.util.*;

public class GenerateMDP {
    //States
    private final String RU8p = "RU8p";
    private final String RU10p = "RU10p";
    private final String RU8a = "RU8a";
    private final String RU10a = "RU10a";

    private final String RD10p = "RD10p";
    private final String RD8a = "RD8a";
    private final String RD10a = "RD10a";

    private final String TU10p = "TU10p";
    private final String TU10a = "TU10a";

    private final String TD10a = "TD10a";


    private Map<String, Integer> rewards;
    private Map<String, Map<String, TransitionTuple>> transitions;
    private Map<String, Double> stateValues;
    private Map<String, Integer> stateVisits;




    public GenerateMDP(){
        // Define states, actions, rewards, and transition probabilities
        String[] states = {RU8p, RU10p, RU8a, RU10a, RD10p, RD8a, RD10a, TU10p, TU10a, TD10a, "11a"};

        this.rewards = new HashMap<>();
        for (String state : states) {
            this.rewards.put(state, 0);
        }

        this.transitions = new HashMap<>();
        this.transitions.put(RU8p, Map.of(
                "P", new TransitionTuple(TU10p, 1.0, 2),
                "R", new TransitionTuple(RU10p, 1.0, 0),
                "S", new TransitionTuple(RD10p, 1.0, -1)
        ));
        this.transitions.put(TU10p, Map.of(
                "P", new TransitionTuple(RD8a, 1.0, 2),
                "R", new TransitionTuple(RD10a, 1.0, 0)
        ));
        this.transitions.put(RU10p, Map.of(
                "R", new TransitionTuple(RU8a, 1, 0),
                "P", new TransitionTuple(RU8a, 0.5, 2),
                "p", new TransitionTuple(RD10a, 0.5, 2),
                "S", new TransitionTuple(RD8a, 1.0, -1)
        ));
        this.transitions.put(RD10p, Map.of(
                "R", new TransitionTuple(RD8a, 1, 0),
                "P", new TransitionTuple(RD8a, 0.5, 2),
                "p", new TransitionTuple(RD10a, 0.5, 2)
        ));
        this.transitions.put(RU8a, Map.of(
                "P", new TransitionTuple(TU10a, 1.0, 2),
                "R", new TransitionTuple(RU10a, 1.0, 0),
                "S", new TransitionTuple(RD10a, 1.0, -1)
        ));
        this.transitions.put(RD8a, Map.of(
                "R", new TransitionTuple(RD10a, 1, 0),
                "P", new TransitionTuple(TD10a, 1, 2)
        ));

        this.transitions.put(TU10a, Map.of("any", new TransitionTuple("11a", 1.0, -1)));
        this.transitions.put(RU10a, Map.of("any", new TransitionTuple("11a", 1.0, 0)));
        this.transitions.put(RD10a, Map.of("any", new TransitionTuple("11a", 1.0, 4)));
        this.transitions.put(TD10a, Map.of("any", new TransitionTuple("11a", 1.0, 3)));

        this.transitions.put("11a", Map.of());

        // Initialize state values and visit counts
        this.stateValues = new HashMap<>();
        this.stateVisits = new HashMap<>();
        for (String state : states) {
            this.stateValues.put(state, 0.0);
            this.stateVisits.put(state, 0);
        }
    }

    public class TransitionTuple {
        private String nextState;
        private double probability;
        private int reward;

        TransitionTuple(String nextState, double probability, int reward) {
            this.nextState = nextState;
            this.probability = probability;
            this.reward = reward;
        }

        public String getNextState() {
            return nextState;
        }

        public void setNextState(String nextState) {
            this.nextState = nextState;
        }

        public double getProbability() {
            return probability;
        }

        public void setProbability(double probability) {
            this.probability = probability;
        }

        public int getReward() {
            return reward;
        }

        public void setReward(int reward) {
            this.reward = reward;
        }
    }
    public String getStartState() {
        return RU8p;
    }
    
    public boolean isTerminalState(String state) {
        // Check if the state is a terminal state
        return state.equals("11a");
    }
    
    public String[] getActions() {
        return new String[]{"P", "R", "S"};
    }
    
    public double getQValue(String state, String action) {
        // Use a method that directly checks the presence of the state-action pair
        return stateValues.getOrDefault(getStateActionPair(state, action), 0.0);
    }
    
    public void setQValue(String state, String action, double value) {
        String stateActionPair = getStateActionPair(state, action);
        stateValues.put(stateActionPair, value);
    }
    
    public double getMaxQValue(String state) {
        return stateValues.getOrDefault(state, 0.0);
    }
    
    public TransitionTuple getTransition(String state, String action) {
        Map<String, TransitionTuple> actionTransitions = transitions.get(state);
    
        if (actionTransitions != null && actionTransitions.containsKey(action)) {
            return actionTransitions.get(action); // Return the corresponding TransitionTuple
        } else {
            return getDefaultTransition(state, action);
        }
    }
    
    public Map<String, Double> getStateValues() {
        return new HashMap<>(stateValues);
    }
    
    private String getStateActionPair(String state, String action) {
        return state + "_" + action;
    }
    
    private TransitionTuple getDefaultTransition(String state, String action) {
        if ("RD10p".equals(state) && "S".equals(action)) {
            return new TransitionTuple("RD8a", 1.0, 0);
        } else if ("TU10p".equals(state) && "S".equals(action)) {
            return new TransitionTuple("RU10a", 1.0, 2);
        } else {
            return new TransitionTuple("11a", 0.0, 0);
        }
    }
    public Map<String, Map<String, TransitionTuple>> getTransitions() {
        return transitions;
    }
    public double getActionProbability(String state, String action) {
        Map<String, TransitionTuple> actionTransitions = transitions.get(state);

        if (actionTransitions != null && actionTransitions.containsKey(action)) {
            return actionTransitions.get(action).getProbability();
        } else {
            return 0.0; // If the action is not present, return 0 probability
        }
    }
    public Map<String, Map<String, TransitionTuple>> getTransitionsValueIteration() {
        // Create a deep copy of the transitions map
        Map<String, Map<String, TransitionTuple>> transitionsCopy = new HashMap<>();

        for (Map.Entry<String, Map<String, TransitionTuple>> entry : transitions.entrySet()) {
            String state = entry.getKey();
            Map<String, TransitionTuple> actionTransitions = entry.getValue();

            // Create a new map for the state in the copy
            Map<String, TransitionTuple> actionTransitionsCopy = new HashMap<>();

            // Copy each transition tuple
            for (Map.Entry<String, TransitionTuple> actionEntry : actionTransitions.entrySet()) {
                String action = actionEntry.getKey();
                TransitionTuple transitionTuple = actionEntry.getValue();
                TransitionTuple transitionTupleCopy = new TransitionTuple(
                        transitionTuple.getNextState(),
                        transitionTuple.getProbability(),
                        transitionTuple.getReward()
                );

                actionTransitionsCopy.put(action, transitionTupleCopy);
            }

            transitionsCopy.put(state, actionTransitionsCopy);
        }

        return transitionsCopy;
    }
}