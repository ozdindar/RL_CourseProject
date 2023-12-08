package rl.base;

public interface RLExperience {
    RLState getState();

    RLAction getAction();

    RLState getNextState();

    double getReward();
}
