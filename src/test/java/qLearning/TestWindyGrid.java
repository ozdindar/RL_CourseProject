package qLearning;

import org.jfree.chart.JFreeChart;
import org.junit.jupiter.api.Test;
import rl.EnvironmentServer;
import rl.base.RLPolicy;
import rl.experiment.*;
import rl.methods.temporaldifference.QLearning;
import rl.methods.temporaldifference.SarsaEpsilon;
import rl.tasks.grid.WindyGrid2D;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TestWindyGrid {

    @Test
    void testSarsa()
    {
        WindyGrid2D wg = new WindyGrid2D(7, 10, new int[]{0, 0, 0, 1, 1, 1, 2, 2, 1, 0});
        wg.setTerminal(3, 7);
        wg.setInitial(3, 0);

        PerformancePlotter pp = new PerformancePlotter("SARSA",600,400,2,600, TrialMode.MOST_RECENT_TRIAL_ONLY, PerformanceMetric.STEPS_PER_EPISODE, PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE, PerformanceMetric.AVERAGE_EPISODE_REWARD);
        pp.startGUI();
        pp.toggleDataCollection(true);

        EnvironmentServer server = new EnvironmentServer(wg,pp);
        server.setStepDelay(20);
        server.setStepDelayStart(2000);

        SarsaEpsilon sarsaEpsilon = new SarsaEpsilon(0.1,0.5,null);


        RLPolicy policy = sarsaEpsilon.run(server, 10000000);

    }

    @Test
    void testSarsaQLearning()
    {
        WindyGrid2D wg = new WindyGrid2D(7, 10, new int[]{0, 0, 0, 1, 1, 1, 2, 2, 1, 0});
        wg.setTerminal(3, 7);
        wg.setInitial(3, 0);

        PerformancePlotter pp = new PerformancePlotter("SARSA",600,400,2,600, TrialMode.MOST_RECENT_TRIAL_ONLY, PerformanceMetric.STEPS_PER_EPISODE, PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE, PerformanceMetric.AVERAGE_EPISODE_REWARD);
        pp.startGUI();
        pp.toggleDataCollection(true);

        EnvironmentServer server = new EnvironmentServer(wg,pp);
        server.setStepDelay(1);
        server.setStepDelayStart(8000);

        SarsaEpsilon sarsaEpsilon = new SarsaEpsilon(0.1,0.5,null);

        QLearning qLearning = new QLearning(0.1, null);
        qLearning.setAlpha(0.5);

        RLPolicy policy = sarsaEpsilon.run(server, 2000);

        pp.startNewAgent("Q-Learning");
        pp.startNewTrial();

        policy = qLearning.run(server, 2000);

        new Scanner(System.in).next();
    }

    @Test
    void testSarsaQLearningExperiement()
    {
        WindyGrid2D wg = new WindyGrid2D(7, 10, new int[]{0, 0, 0, 1, 1, 1, 2, 2, 1, 0});
        wg.setTerminal(3, 7);
        wg.setInitial(3, 0);

        SarsaEpsilon sarsaEpsilon = new SarsaEpsilon(0.1,0.5,null);

        QLearning qLearning = new QLearning(0.1, null);
        qLearning.setAlpha(0.5);

        Map<String, List<Trial>> dataMap = ControlExperiment.perform(wg, Arrays.asList(sarsaEpsilon,qLearning),100,1000);

        JFreeChart chart = ExperimentChartUtils.cumulativeStepEpisodeChart("Cumulative Step Count Comparison",dataMap);
        ExperimentChartUtils.saveChart(chart,"./out/cumStep.png",600,400);
    }

}
