package rl.base;

import org.apache.commons.math3.util.Pair;
import rl.SimpleRLExperience;

import java.util.ArrayList;
import java.util.List;

public interface RLEnvironment {

    RLState currentState();
    RLState reset();

    Pair<RLState,Double> step(RLAction action);
    List<? extends RLAction> getActions(RLState state);
    boolean isTerminal(RLState state);

    default List<? extends RLAction> getActions() {
        return getActions(currentState());
    }

    default boolean isOver()
    {
        return isTerminal(currentState());
    }

    default List<RLExperience> generateEpisode(RLPolicy policy)
    {
        List<RLExperience> episode = new ArrayList<>();
        reset();

        while (!isOver())
        {
            RLState s = currentState();
            RLAction a = policy.getAction(s);
            Pair<RLState,Double> obs = step(a);
            episode.add(new SimpleRLExperience(s,a,obs.getFirst(),obs.getSecond()));
        }
        return episode;
    }
}
