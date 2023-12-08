package rl.multiarmbandit;

public class GreedyAC implements ActionChooser {
    @Override
    public int getAction(double[] values) {
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

    @Override
    public String getName() {
        return "Greedy";
    }
}
