package Application;
import Algorithms.MonteCarlo.MonteCarlo;
import Algorithms.QLearning.QLearning;
import Algorithms.ValueIteration.ValueIteration;

public class Main {
  public static void main(String[] args) {
    //Monte Carlo Search
    System.out.println("--------MonteCarloSearch--------");
    MonteCarlo mc = new MonteCarlo();
    mc.simulate();
    System.out.println();

    //Value Iteration Search
    System.out.println("--------ValueIterationSearch--------");
    ValueIteration vi = new ValueIteration();
    vi.valueIterate();
    System.out.println();

    //Q-Learning Search
    System.out.println("--------QLearningSearch--------");
    QLearning ql = new QLearning();
    ql.runQLearning();
    System.out.println();
  }
}