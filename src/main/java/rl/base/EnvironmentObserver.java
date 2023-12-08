package rl.base;

import org.apache.commons.math3.util.Pair;



public interface EnvironmentObserver {
    void environmentReset(RLEnvironment environment);
    void environmentStep(Pair<RLState, Double> result);
}
