package rl.methods.functionapprox.linear;

import rl.base.RLAction;
import rl.base.RLState;

public interface FeatureExtractor {

    double[] featuresOf(RLState s);
    double[] featuresOf(RLState s, RLAction a);


    int featureCount(boolean actionsIncluded);
}
