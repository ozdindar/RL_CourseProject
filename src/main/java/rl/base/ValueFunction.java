package rl.base;

import java.util.Set;

public interface ValueFunction {
    Set<RLState> getStates();
    double getValue(RLState s);
}
