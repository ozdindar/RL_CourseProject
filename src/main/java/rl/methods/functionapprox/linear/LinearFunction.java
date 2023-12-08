package rl.methods.functionapprox.linear;

import java.util.Random;

public class LinearFunction implements ApproximationFunction{

    private final int numOfWeights;

    double[] weights;

    LinearFunction(int numOfWeights)
    {
        this.numOfWeights = numOfWeights;
        initWeights();
    }


    public void initWeights() {
        Random rand = new Random();
        weights= new double[numOfWeights];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = rand.nextDouble() * 2 - 1; // Random value between -1 and 1
        }
    }

    @Override
    public double value(double[] features) {
        double val = 0;

        for (int i = 0; i < features.length; i++) {
            val += features[i]*weights[i];
        }

        return val;
    }

    @Override
    public double[] gradient(double[] features) {
        double [] gradients = new double[weights.length];
        for (int i = 0; i < gradients.length; i++) {
            gradients[i] = features[i];
        }
        return gradients;
    }

    @Override
    public void updateWeights(double[] features, double error,double learningRate) {
        double[] gradient= gradient(features);
        for (int i = 0; i < weights.length; i++) {
            weights[i] = weights[i]+ (learningRate*error)*gradient[i]; // w -> w + alpha * (Gt - v-hat(s,w)) * Delta_v-hat()
            // x(s) is the gradients of v-hat(s,w) in linear function
        }
    }

}
