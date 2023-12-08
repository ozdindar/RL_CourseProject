package rl;

import rl.base.DeterministicPolicy;
import rl.base.RLPolicy;
import rl.base.RLPolicyRenderer;
import rl.base.RLState;

public class DeterministicPolicyRenderer implements RLPolicyRenderer {

    @Override
    public void renderPolicy(RLPolicy pi) {
        DeterministicPolicy dp = (DeterministicPolicy) pi;
        for (RLState state:dp.getStates())
            System.out.println(state + " : " + dp.getAction(state));
    }
}
