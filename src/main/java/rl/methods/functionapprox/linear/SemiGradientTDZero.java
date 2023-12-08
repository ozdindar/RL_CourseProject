package rl.methods.functionapprox.linear;

import org.apache.commons.math3.util.Pair;
import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLPolicy;
import rl.base.RLState;
import rl.tasks.randomwalk.RWUniformRandomPolicy;
import rl.tasks.randomwalk.RandomWalkLong;

import java.security.SecureRandom;
import java.util.Random;

public class SemiGradientTDZero {
    Random rng = new SecureRandom();

    double alpha = 0.1;
    double gamma = 1.0;

    FeatureExtractor featureExtractor;
    ApproximationFunction vFunction;
    double[] w;

    RLPolicy policy;

    ApproximationFunctionRenderer functionRenderer;


    public SemiGradientTDZero(FeatureExtractor featureExtractor, ApproximationFunction vFunction, RLPolicy policy) {
        this.featureExtractor = featureExtractor;
        this.vFunction = vFunction;
        this.policy = policy;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public void setFunctionRenderer(ApproximationFunctionRenderer functionRenderer) {
        this.functionRenderer = functionRenderer;
    }
    private void initW() {
        w = new double[featureExtractor.featureCount(false)];
    }

    private void updateW(RLState s,double reward,RLState ns) {
        double[] s_features = featureExtractor.featuresOf(s);
        double oldValue = vFunction.value(s_features);// v-hat(s,w)

        double[] ns_features = featureExtractor.featuresOf(ns);
        double ns_v= vFunction.value(ns_features);

        vFunction.updateWeights(s_features,reward + gamma*ns_v -oldValue,alpha);
    }

    public void run(RLEnvironment env, int episodeCount)
    {
        initW();

        for (int e = 0; e < episodeCount; e++) {
            RLState s = env.reset();
            while (!env.isTerminal(s))
            {
                RLAction a = policy.getAction(s);
                Pair<RLState,Double> next = env.step(a);
                RLState ns = next.getFirst();
                double r = next.getSecond();

                updateW(s,r,ns);

                s = ns;
            }
        }

        if (functionRenderer!= null)
            functionRenderer.render(env,vFunction,featureExtractor,w);

    }

    public static void main(String[] args) {
        RandomWalkLong rwl = new RandomWalkLong(1000,500);
        SemiGradientTDZero sgtd = new SemiGradientTDZero(new RWFeatureExtractor(1000,100),new LinearFunction(10),new RWUniformRandomPolicy());
        sgtd.setAlpha(0.00001);
        sgtd.setGamma(1.0);
        sgtd.setFunctionRenderer(new RWAFRenderer("valuePlot.jpg"));
        sgtd.run(rwl,1000000);

    }

}
