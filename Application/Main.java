package Application;
import Algorithms.MonteCarlo.MonteCarlo;
import Algorithms.QLearning.QLearning;
import MDP.GenerateMDP;
public class Main {
  public static void main(String[] args) {
    MonteCarlo mc = new MonteCarlo();
    GenerateMDP mdp = new GenerateMDP();
        int numEpisodes = 1000;
        double alpha = 0.2;
        double gamma = 0.99;
        double epsilon = 0.1;
        QLearning.runQLearning(mdp, numEpisodes, alpha, gamma, epsilon);
  }
}
