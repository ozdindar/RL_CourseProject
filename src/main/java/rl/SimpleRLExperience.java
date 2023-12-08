package rl;

import rl.base.RLAction;
import rl.base.RLExperience;
import rl.base.RLState;

public class SimpleRLExperience implements RLExperience {
    RLState state;
    RLAction action;
    RLState nextState;
    double reward;

    public SimpleRLExperience(RLState state, RLAction action, RLState nextState, double reward) {
        this.state = state;
        this.action = action;
        this.nextState = nextState;
        this.reward = reward;
    }

    public RLState getState() {
        return state;
    }

    public RLAction getAction() {
        return action;
    }

    public RLState getNextState() {
        return nextState;
    }

    public double getReward() {
        return reward;
    }

    @Override
    public String toString() {
        return state + "-" + action + " ->("+ nextState+ "-"+ reward + ")";
    }
}
