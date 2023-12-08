package rl.tasks.recycler;

import rl.base.*;

public class BotPolicyRenderer implements RLPolicyRenderer {
    @Override
    public void renderPolicy(EnvironmentModel environment, RLPolicy pi) {
        renderPolicy(pi);
    }

    @Override
    public void renderPolicy(RLPolicy pi) {
        DeterministicPolicy dp = (DeterministicPolicy) pi;
        for (RLState rlState:dp.getStates())
        {
            System.out.print(dp.getAction(rlState).name() + "     ");
        }
        System.out.println();
    }
}
