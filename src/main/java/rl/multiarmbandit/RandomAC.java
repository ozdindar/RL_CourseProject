package rl.multiarmbandit;

import java.security.SecureRandom;
import java.util.Random;

public class RandomAC implements ActionChooser {

    Random r = new SecureRandom();


    @Override
    public int getAction(double[] values) {
        return r.nextInt(values.length);
    }

    @Override
    public String getName() {
        return "Random";
    }
}
