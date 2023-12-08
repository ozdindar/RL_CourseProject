package rl.methods.functionapprox.linear;

import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLPolicy;
import rl.base.RLState;

import java.util.Collections;
import java.util.List;

public class QFunctionPolicy implements RLPolicy {
    FeatureExtractor extractor;
    ApproximationFunction qFunction;
    RLEnvironment environment;

    public QFunctionPolicy(FeatureExtractor extractor, ApproximationFunction qFunction, RLEnvironment environment) {
        this.extractor = extractor;
        this.qFunction = qFunction;
        this.environment = environment;
    }

    private double getQValue(RLEnvironment env, RLState s, RLAction a) {
        double[] sa_features= extractor.featuresOf(s,a);
        return qFunction.value(sa_features);

    }
    private RLAction greedySelection(RLEnvironment env,RLState s, List<? extends RLAction> actions) {

        Collections.shuffle(actions);
        RLAction bestAction =actions.stream().max((a1,a2)->Double.compare(getQValue(env,s,a1),getQValue(env,s,a2))).get();
        return bestAction;
    }

    @Override
    public RLAction getAction(RLState s) {
        List<? extends RLAction> actions = environment.getActions(s);
        return greedySelection(environment,s,actions);
    }
}
