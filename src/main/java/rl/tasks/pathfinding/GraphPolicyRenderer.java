package rl.tasks.pathfinding;

import rl.base.*;

public class GraphPolicyRenderer implements RLPolicyRenderer {
    @Override
    public void renderPolicy(EnvironmentModel environment, RLPolicy pi) {
            renderPolicy(pi);
                }

    @Override
    public void renderPolicy(RLPolicy pi) {
        DeterministicPolicy dp = (DeterministicPolicy) pi;
        for (RLState state:dp.getStates()) {
            System.out.println(state + " : " + dp.getAction(state));

        }
    }
}
