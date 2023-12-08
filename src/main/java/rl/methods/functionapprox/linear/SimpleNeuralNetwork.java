package rl.methods.functionapprox.linear;

import java.util.Random;

public class SimpleNeuralNetwork implements ApproximationFunction {

    private final int numInput;    // Number of input features
    private final int numHidden;   // Number of neurons in the hidden layer
    private final int numOutput;   // Number of neurons in the output layer

    double[] w;

    public SimpleNeuralNetwork(int numInput, int numHidden, int numOutput) {
        this.numInput = numInput;
        this.numHidden = numHidden;
        this.numOutput = numOutput;
        initWeights();
    }

    private double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    private double sigmoidDerivative(double x) {
        double sigmoidX = sigmoid(x);
        return sigmoidX * (1.0 - sigmoidX);
    }

    @Override
    public void initWeights()
    {
        Random rand = new Random();
        w= new double[numOfWeights()];
        for (int i = 0; i < w.length; i++) {
            w[i] = 0.5;//rand.nextDouble() * 2 - 1; // Random value between -1 and 1
        }
    }



    private void calculateActivations(double[] features, double[] hiddenLayer, double[] outputLayer) {
        int w1Index = 0; // Index for weights between input and hidden layer
        int w2Index = numInput * numHidden; // Index for weights between hidden and output layer
        // Calculate activations of the hidden layer
        for (int i = 0; i < numHidden; i++) {
            double sum = 0.0;
            for (int j = 0; j < numInput; j++) {
                sum += w[w1Index++] * features[j];
            }
            hiddenLayer[i] = sigmoid(sum);
        }

        // Calculate activations of the output layer
        for (int i = 0; i < numOutput; i++) {
            double sum = 0.0;
            for (int j = 0; j < numHidden; j++) {
                sum += w[w2Index++] * hiddenLayer[j];
            }
            outputLayer[i] = sigmoid(sum);
        }
    }

    @Override
    public double value(double[] features) {
        double[] hiddenLayer = new double[numHidden];
        double[] outputLayer = new double[numOutput];

        calculateActivations(features, hiddenLayer, outputLayer);

        return outputLayer[0]; // Assuming a single output neuron
    }

    @Override
    public double[] gradient(double[] features) {
        double[] hiddenLayer = new double[numHidden];
        double[] outputLayer = new double[numOutput];

        // Calculate activations
        calculateActivations(features, hiddenLayer, outputLayer);

        // Calculate the gradient of the weights
        double[] gradient = new double[w.length];
        int index = 0;


        // Calculate the gradient for the weights between input and hidden layer
        for (int i = 0; i < numHidden; i++) {
            for (int j = 0; j < numInput; j++) {
                double hiddenLayerGradient = w[index] * sigmoidDerivative(hiddenLayer[i]);
                gradient[index++] = features[j] * hiddenLayerGradient;
            }
        }

        // Calculate the gradient for the weights between hidden and output layer
        for (int i = 0; i < numOutput; i++) {
            for (int j = 0; j < numHidden; j++) {
                gradient[index++] = hiddenLayer[j] * sigmoidDerivative(outputLayer[i]);
            }
        }

        return gradient;
    }

    @Override
    public void updateWeights(double[] features, double error, double learningRate ) {
        double[] gradient = gradient(features);

        for (int i = 0; i < w.length; i++) {
            w[i] += learningRate * error * gradient[i];
        }
    }

    public int numOfWeights() {
        return numHidden*numInput + numOutput*numHidden;
    }
}