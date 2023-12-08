package testMultiarmBandit;

import org.junit.jupiter.api.Test;
import rl.multiarmbandit.*;

import java.io.IOException;


public class TestBanditExperiment {

    /**
     * Creates an experiment with 3 different Action selection method.
     * Runs the experiment 1000 times for each algorithm. Creates the comparison plot
     * See the chart file : Experiment_Result_Chart.jpeg after running this test
     * @throws IOException
     */
    @Test
    void testSampleAverageExperiment() throws IOException {
        Bandit bandit =new MultiArmBandit(10);

        BanditExperiment experiment = new BanditExperiment(bandit,2000);
        experiment.addPlayer(new RLBanditPlayer("P1",new SampleAverageVE(),new EpsilonGreedyAC(0.9)));
        experiment.addPlayer(new RLBanditPlayer("P2",new SampleAverageVE(),new GreedyAC()));
        experiment.addPlayer(new RLBanditPlayer("P2",new SampleAverageVE(),new RandomAC()));


        experiment.performExperiment(1000);
    }

    /**
     * Creates an experiment with 3 different Action selection method.
     * Runs the experiment 1000 times for each algorithm. Creates the comparison plot
     * See the chart file : Experiment_Result_Chart.jpeg after running this test
     * @throws IOException
     */
    @Test
    void testConstantStepSizeExperiment() throws IOException {
        Bandit bandit =new MultiArmBandit(10);

        BanditExperiment experiment = new BanditExperiment(bandit,2000,"ConstanStepSize_Chart.jpeg");
        experiment.addPlayer(new RLBanditPlayer("P1",new ConstantStepSizeVE(0.9),new EpsilonGreedyAC(0.9)));
        experiment.addPlayer(new RLBanditPlayer("P2",new ConstantStepSizeVE(0.5),new EpsilonGreedyAC(0.9)));
        experiment.addPlayer(new RLBanditPlayer("P3",new SampleAverageVE(),new EpsilonGreedyAC(0.9)));


        experiment.performExperiment(1000);
    }

}
