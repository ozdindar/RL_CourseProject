package rl;

import rl.base.RLState;
import rl.base.RLTransition;

public class SimpleTransition implements RLTransition {
    private RLState fromState;
    private RLState toState;
    private double prob;
    private double reward;

    public SimpleTransition(RLState fromState, RLState toState, double prob, double reward) {
        this.fromState = fromState;
        this.toState = toState;
        this.prob = prob;
        this.reward = reward;
    }

    @Override
    public RLState from() {
        return fromState;
    }

    @Override
    public RLState to() {
        return toState;
    }

    @Override
    public double prob() {
        return prob;
    }

    @Override
    public double reward() {
        return reward;
    }
}
