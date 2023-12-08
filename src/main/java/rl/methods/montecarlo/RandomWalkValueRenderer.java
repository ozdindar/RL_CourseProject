package rl.methods.montecarlo;

import rl.base.RLEnvironment;
import rl.base.RLState;
import rl.base.RLValueRenderer;
import rl.tasks.randomwalk.RWState;

import java.util.HashMap;
import java.util.Map;

public class RandomWalkValueRenderer implements RLValueRenderer {
    @Override
    public void renderValues(RLEnvironment environment, HashMap<RLState, Double> v) {

    }

    @Override
    public void renderValues(Map<RLState, Double> v) {
        for (RLState state: v.keySet())
        {
            RWState rws = (RWState) state;
            System.out.println(rws+ " : " + v.get(state));
        }

    }
}
