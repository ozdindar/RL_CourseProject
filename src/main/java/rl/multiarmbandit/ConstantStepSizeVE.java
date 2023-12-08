package rl.multiarmbandit;


import java.util.Arrays;

public class ConstantStepSizeVE implements ValueEstimator {

    double[] estimations;
    private double initialEstimation;
    private double stepSize;

    public ConstantStepSizeVE(double stepSize) {
        this.stepSize = stepSize;
    }

    @Override
    public double[] getValues() {
        return estimations;
    }

    @Override
    public void feed(int a, double reward) {
        estimations[a] += stepSize*(reward-estimations[a]);
    }

    @Override
    public void init(int armCount) {
        estimations = new double[armCount];
        Arrays.fill(estimations, initialEstimation);
    }

    @Override
    public String getName() {
        return "ConstantStepSize";
    }
}
