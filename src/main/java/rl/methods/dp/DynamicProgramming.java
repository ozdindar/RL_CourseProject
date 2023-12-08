package rl.methods.dp;

import rl.base.EnvironmentModel;

public interface DynamicProgramming
{
    void perform(EnvironmentModel model, int iteration);
}
