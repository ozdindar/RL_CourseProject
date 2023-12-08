package rl.multiarmbandit;

import java.util.Arrays;

public class SampleAverageVE implements ValueEstimator {

    double initialEstimation;
    double[] estimations;
    int[] updateTimes;

    @Override
    public double[] getValues() {
        return estimations;
    }

    @Override
    public void feed(int a, double reward) {
        estimations[a] += (reward-estimations[a])/updateTimes[a];
        updateTimes[a]++;
    }

    @Override
    public void init(int armCount) {
        estimations = new double[armCount];
        updateTimes = new int[armCount];
        Arrays.fill(estimations, initialEstimation);
        Arrays.fill(updateTimes,1);
    }

    @Override
    public String getName() {
        return "Sample Average";
    }
}
