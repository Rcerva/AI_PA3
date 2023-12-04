package Application;
import Algorithms.MonteCarlo.MonteCarlo;
import Algorithms.QLearning.QLearning;
import MDP.GenerateMDP;
public class Main {
  public static void main(String[] args) {
    //Monte Carlo Search
    MonteCarlo mc = new MonteCarlo();
    mc.simulate();
    //Value Iteration Search

    //Q-Learning Search
        GenerateMDP mdp = new GenerateMDP();
//         int numEpisodes = 1000;
//         double alpha = 0.2;
//         double gamma = 0.99;
//         double epsilon = 0.1;

  }
}