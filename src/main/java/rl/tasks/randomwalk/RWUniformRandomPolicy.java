package rl.tasks.randomwalk;

import rl.base.RLAction;
import rl.base.RLPolicy;
import rl.base.RLState;

import java.security.SecureRandom;
import java.util.Random;

public class RWUniformRandomPolicy implements RLPolicy {

    Random rng = new SecureRandom();

    @Override
    public RLAction getAction(RLState s) {
        return rng.nextBoolean()?RWAction.RightAction:RWAction.LeftAction;
    }



}
