package rl.methods.functionapprox.linear;

import rl.base.RLAction;
import rl.base.RLState;
import rl.tasks.randomwalk.RWAction;
import rl.tasks.randomwalk.RWDirection;
import rl.tasks.randomwalk.RWState;

public class RWFeatureExtractor  implements FeatureExtractor{

    int pathLength;
    int groupSize;

    public RWFeatureExtractor(int pathLength, int groupSize) {
        this.pathLength = pathLength;
        this.groupSize= groupSize;
    }

    @Override
    public double[] featuresOf(RLState s) {
        RWState rws = (RWState) s;

        double[] features = new double[pathLength/groupSize];
        features[rws.getIndex()/groupSize]=1;
        return features;
    }

    @Override
    public double[] featuresOf(RLState s, RLAction a) {
        RWState rws = (RWState) s;
        RWAction rwa= (RWAction) a;

        double[] features = new double[2*pathLength/groupSize];
        int offset = rwa.getDir()== RWDirection.Left ? 0:pathLength/groupSize;
        features[offset+rws.getIndex()/groupSize]=1;
        return features;
    }

    @Override
    public int featureCount(boolean b) {
        return (b ? 2:1) * (pathLength/groupSize);
    }
}
