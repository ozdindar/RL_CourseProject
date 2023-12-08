package rl.methods.dyna;

import org.apache.commons.math3.util.Pair;
import rl.base.RLAction;
import rl.base.RLExperience;
import rl.base.RLState;

import java.util.List;
import java.util.Set;

public interface DeterministicModel {
    void learn(RLExperience experience);

    Set<RLState> getExperiencedStates();

    Set<RLAction> getExperiencedActions(RLState state);

    RLExperience getExperience(RLState state, RLAction action);

    List<Pair<RLState, RLAction>> getPredecessors(RLState state);
}
