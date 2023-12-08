package rl.tasks.randomwalk;

import org.apache.commons.math3.util.Pair;
import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLState;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomWalk implements RLEnvironment {

    private static final double WIN_REWARD = 1.0;
    private static final double NO_REWARD = 0.0;
    Random rng = new SecureRandom();

    RWState[] states;
    int currentIndex;

    public RandomWalk(int pathLength, int initialIndex) {
        states = new RWState[pathLength];
        for (int i = 0; i < pathLength; i++) {
            states[i] = new RWState(i);
        }

    }

    //@Override
    public List<RLState> getStates() {
        return Arrays.asList(states);
    }

    @Override
    public RLState currentState() {
        return states[currentIndex];
    }

    @Override
    public RLState reset() {
        currentIndex = states.length/2;
        return states[currentIndex];
    }

    @Override
    public Pair<RLState, Double> step(RLAction action) {
        RWState rws = states[currentIndex];
        RWAction rwa = (RWAction) action;

        RLState next= rws;
        double reward = NO_REWARD;

        if (rwa.dir == RWDirection.Left){
            if (rws.index>0)
                currentIndex--;

        }
        if (rwa.dir == RWDirection.Right){
            if (rws.index<states.length-1) {
                currentIndex++;
                if (rws.index+1== states.length-1)
                    reward= WIN_REWARD;
            }
        }
        return Pair.create(states[currentIndex],reward);
    }

    private RLState getState(int i) {
        return states[i];
    }

    @Override
    public List<? extends RLAction> getActions(RLState state) {
        if (!isTerminal(state))
            return Arrays.asList(RWAction.LeftAction,RWAction.RightAction);
        return List.of();
    }

    @Override
    public boolean isTerminal(RLState state) {
        RWState rws = (RWState) state;
        return rws.index==0 || rws.index== states.length-1;
    }


}
