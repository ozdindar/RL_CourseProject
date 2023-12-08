package rl.base;

import java.util.List;

public interface EnvironmentModel {
    List<? extends RLTransition> getTransitions(RLState state, RLAction action);
    List<RLState> getStates();
    List<? extends RLAction> getActions(RLState state);
    boolean isTerminal(RLState state);

    default double getExpectedReward(RLState state, RLAction action)
    {
        double reward =0;
        for(RLTransition transition:getTransitions(state,action))
        {
            reward += transition.prob()*transition.reward();
        }
        return reward;
    }
}
