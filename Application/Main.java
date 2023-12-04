package Application;
import Algorithms.MonteCarlo.MonteCarlo;
import Algorithms.QLearning.QLearning;
import Algorithms.ValueIteration.ValueIteration;

public class Main {
  public static void main(String[] args) {
    System.out.println("\n");
    //Monte Carlo Search
    System.out.println("--------MonteCarloSearch--------");
    MonteCarlo mc = new MonteCarlo();
    mc.simulate();
    System.out.println("\n\n\n");

    //Value Iteration Search
    System.out.println("--------ValueIterationSearch--------");
    ValueIteration vi = new ValueIteration();
    vi.valueIterate();
    System.out.println("\n\n\n");

    //Q-Learning Search
    System.out.println("--------QLearningSearch--------");
    QLearning ql = new QLearning();
    ql.runQLearning();
    System.out.println("\n");

  }
}