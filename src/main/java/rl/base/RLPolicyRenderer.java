package rl.base;

public interface RLPolicyRenderer {
    default void renderPolicy(RLEnvironment model, RLPolicy pi){
        renderPolicy(pi);
    }

    default void renderPolicy(EnvironmentModel model, RLPolicy pi)
    {
        renderPolicy(pi);
    }
    void renderPolicy( RLPolicy pi);
}
