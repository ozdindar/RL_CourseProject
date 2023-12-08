package rl.methods.functionapprox.linear;

public interface ApproximationFunction {
    void initWeights();

    double value(double[] features);

    double[]gradient ( double features[]);

    void updateWeights(double[] features,double error, double learningRate);

}
