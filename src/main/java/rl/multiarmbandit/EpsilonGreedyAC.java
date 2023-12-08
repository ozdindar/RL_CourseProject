package rl.multiarmbandit;

import java.security.SecureRandom;
import java.util.Random;

public class EpsilonGreedyAC implements ActionChooser {

    Random rng = new SecureRandom();
    double epsilon;

    public EpsilonGreedyAC(double epsilon) {
        this.epsilon = epsilon;
    }

    @Override
    public int getAction(double[] values) {
        if (rng.nextDouble()<epsilon)
            return greedyAction(values);
        else return rng.nextInt(values.length);

    }

    @Override
    public String getName() {
        return "Epsilon-Greedy";
    }

    private int greedyAction(double[] values) {
        int maxIndex =0;
        double max= values[0];
        for (int i = 1; i < values.length; i++) {
            if (values[i]>max)
            {
                max = values[i];
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
