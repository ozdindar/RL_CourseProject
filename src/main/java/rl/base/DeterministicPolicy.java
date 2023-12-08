package rl.base;

import java.util.Set;

public interface DeterministicPolicy extends RLPolicy {
    void setAction(RLState state, RLAction action);

    boolean containsActionFor(RLState state);

    Set<RLState> getStates();

    RLAction getAction( RLState s);
}
