package rl.tasks.rod;

import rl.base.RLAction;
import rl.base.RLState;
import rl.methods.functionapprox.linear.FeatureExtractor;

public class RMFeatureExtractor implements FeatureExtractor {
    @Override
    public double[] featuresOf(RLState s) {
        RodState rs = (RodState) s;
        return new double[]{rs.x,rs.y,rs.angle};
    }

    @Override
    public double[] featuresOf(RLState s, RLAction a) {
        RodState rs = (RodState) s;
        RodAction ra= (RodAction) a;
        return new double[] {rs.x,rs.y,rs.angle,((RodAction) a).ordinal()};
    }

    @Override
    public int featureCount(boolean actionsIncluded) {
        return actionsIncluded ? 4:3;
    }
}
