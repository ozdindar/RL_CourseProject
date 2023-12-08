package rl.base;

import java.util.HashMap;
import java.util.Map;

public interface RLValueRenderer {

    default void renderValues(RLEnvironment environment, HashMap<RLState,Double> v)
    {
        renderValues(v);
    }

    default void renderValues(EnvironmentModel model, HashMap<RLState, Double> v)
    {
        renderValues(v);
    }
    void renderValues(Map<RLState, Double> v);
}
