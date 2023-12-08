package rl.methods.functionapprox.linear;

import rl.base.RLEnvironment;

public interface ApproximationFunctionRenderer {
    void render(RLEnvironment env, ApproximationFunction vFunction, FeatureExtractor featureExtractor, double[] w);
}
