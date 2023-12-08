package rl.base;

public interface RLTransition {
    RLState from();
    RLState to();
    double prob();

    double reward();
}
