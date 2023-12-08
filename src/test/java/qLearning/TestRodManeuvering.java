package qLearning;

import org.junit.jupiter.api.Test;
import rl.AvgEpisodeCounTracker;
import rl.EnvironmentServer;
import rl.base.EnvironmentObserver;
import rl.base.RLEnvironment;
import rl.experiment.PerformanceMetric;
import rl.experiment.PerformancePlotter;
import rl.experiment.TrialMode;
import rl.methods.dyna.TabularDynaQ;
import rl.methods.temporaldifference.QLearning;
import rl.tasks.rod.RodManeuvering;
import rl.tasks.rod.RodPanel;
import rl.tasks.rod.RodState;

import javax.swing.*;
import java.awt.*;

public class TestRodManeuvering {

    public static RodManeuvering buildRodEnvironment()
    {
        RodState initial = new RodState(3,15,9);
        RodState terminal = new RodState(30,20,0);

        RodManeuvering rm = new RodManeuvering(400,400,initial,terminal);

        rm.addObstacle(new Polygon(new int[]{120,200,250,170},new int[]{20,80,60,30},4));

        rm.addObstacle(new Polygon(new int[]{150,370,370,200,200,150},new int[]{120,120,150,150,370,370},6));

        rm.addObstacle(new Polygon(new int[]{260,360,360,260},new int[]{240,240,280,280},4));

        return rm;
    }

    @Test
    void testRodManeuveringQLearning()
    {
        RLEnvironment environment = buildRodEnvironment();
        RodPanel rodPanel = new RodPanel((RodManeuvering) environment);

        JFrame main =new JFrame("Rod MAneuvering");
        main.add(rodPanel);
        main.setSize(1200,800);
        main.setVisible(true);




        EnvironmentServer server = new EnvironmentServer(environment,rodPanel);

        QLearning qLearning = new QLearning(0.8,null);
        qLearning.setPauseInterval(0);
        qLearning.run(server,1000000);

        TabularDynaQ dynaQ = new TabularDynaQ(0.1,0.95,0.1,100);
    }

    @Test
    void testRodManeuveringDyna()
    {
        RLEnvironment environment = buildRodEnvironment();
        RodPanel rodPanel = new RodPanel((RodManeuvering) environment);

        JFrame main =new JFrame("Rod MAneuvering");
        main.add(rodPanel);
        main.setSize(1200,800);
        main.setVisible(true);

        PerformancePlotter pp = new PerformancePlotter("Q-Learning",600,400,2,600, TrialMode.MOST_RECENT_TRIAL_ONLY, PerformanceMetric.STEPS_PER_EPISODE, PerformanceMetric.CUMULATIVE_STEPS_PER_EPISODE, PerformanceMetric.AVERAGE_EPISODE_REWARD);
        pp.startGUI();
        pp.toggleDataCollection(true);

        EnvironmentObserver avepTracker =  new AvgEpisodeCounTracker();
        EnvironmentServer server = new EnvironmentServer(environment,rodPanel,avepTracker,pp);
        server.setStepDelay(200);
        server.setStepDelayStart(20000000);

        TabularDynaQ dynaQ = new TabularDynaQ(0.1,0.95,0.1,100);
        //dynaQ.setTheta(0.01);

        dynaQ.run(server,1000000);
    }


}
