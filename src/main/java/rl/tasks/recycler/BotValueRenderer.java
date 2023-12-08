package rl.tasks.recycler;

import rl.base.EnvironmentModel;
import rl.base.RLState;
import rl.base.RLValueRenderer;

import java.util.HashMap;
import java.util.Map;

public class BotValueRenderer implements RLValueRenderer {
    @Override
    public void renderValues(EnvironmentModel environment, HashMap<RLState, Double> v) {
        RecycleRobot robot = (RecycleRobot) environment;

        for (RLState state :v.keySet())
        {
            System.out.printf("% 6.5f\t",v.get(state));
        }
        System.out.println("\n");
    }

    @Override
    public void renderValues(Map<RLState, Double> v) {
        for (RLState state :v.keySet())
        {
            System.out.printf("% 6.5f\t",v.get(state));
        }
        System.out.println("\n");
    }
}
