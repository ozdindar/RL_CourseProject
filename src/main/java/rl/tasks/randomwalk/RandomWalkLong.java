package rl.tasks.randomwalk;

import org.apache.commons.math3.util.Pair;
import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLState;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RandomWalkLong implements RLEnvironment {

    private static final double WIN_REWARD = 1.0;
    private static final double NO_REWARD = 0.0;
    private static final double LOSS_REWARD = -1.0;

    private static final int MAX_STEP = 100;

    Random rng = new SecureRandom();

    RWState[] states;

    int initialIndex;
    int currentIndex;

    public RandomWalkLong(int pathLength, int initialIndex) {
        states = new RWState[pathLength];
        for (int i = 0; i < pathLength; i++) {
            states[i] = new RWState(i);
        }
        this.initialIndex = initialIndex;
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
        currentIndex = initialIndex;
        return states[currentIndex];
    }

    @Override
    public Pair<RLState,Double> step(RLAction action) {
        RWState rws = (RWState) states[currentIndex];
        RWAction rwa = (RWAction) action;

        int stepSize = 1+rng.nextInt(MAX_STEP);

        //int currentIndex = rws.index;
        double reward= NO_REWARD;
        if (rwa.dir == RWDirection.Left){
            currentIndex -= stepSize;
            currentIndex = Math.max(currentIndex,0);

            if (currentIndex==0)
                reward = LOSS_REWARD;
        }
        if (rwa.dir == RWDirection.Right){
            currentIndex+=stepSize;
            currentIndex = Math.min(currentIndex,states.length-1);

            if (currentIndex==states.length-1)
                reward = WIN_REWARD;
        }

        return Pair.create(getState(currentIndex),reward);
    }

    public RLState getState(int i) {
        return states[i];
    }

    @Override
    public List<? extends RLAction> getActions(RLState state) {
        return Arrays.asList(RWAction.LeftAction,RWAction.RightAction);
    }

    @Override
    public boolean isTerminal(RLState state) {
        RWState rws = (RWState) state;
        return rws.index==0 || rws.index== states.length-1;
    }


    public int pathLength() {
        return states.length;
    }
}
