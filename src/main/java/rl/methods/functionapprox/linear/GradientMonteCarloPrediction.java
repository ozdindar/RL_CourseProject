package rl.methods.functionapprox.linear;

import rl.base.RLEnvironment;
import rl.base.RLExperience;
import rl.base.RLPolicy;
import rl.base.RLState;
import rl.tasks.randomwalk.RWUniformRandomPolicy;
import rl.tasks.randomwalk.RandomWalkLong;

import java.util.List;

public class GradientMonteCarloPrediction {

    private static final double DISCOUNT_RATE = 1.0;
    FeatureExtractor featureExtractor;
    ApproximationFunction vFunction;
    double[] w;

    double alpha;


    public void setFunctionRenderer(ApproximationFunctionRenderer functionRenderer) {
        this.functionRenderer = functionRenderer;
    }

    private ApproximationFunctionRenderer functionRenderer;

    public GradientMonteCarloPrediction(FeatureExtractor featureExtractor, ApproximationFunction vFunction, double alpha) {
        this.featureExtractor = featureExtractor;
        this.vFunction = vFunction;
        this.alpha = alpha;

    }


    private void updateValues(List<RLExperience> episode) {
        double returns =0;
        for (int i = episode.size()-1; i>=0; i--) {
            RLExperience step = episode.get(i);
            RLState s= step.getState();
            returns = returns*DISCOUNT_RATE +  step.getReward();
            updateW(s,returns);
        }
    }

    private void updateW(RLState s,double returns) {
        double[] features = featureExtractor.featuresOf(s);
        double oldValue = vFunction.value(features);// v-hat(s,w)

        double[] gradientOfVHat= vFunction.gradient(features);

        vFunction.updateWeights(features,returns-oldValue,alpha);

    }

    public void run(RLEnvironment env, RLPolicy policy, int episodeCount)
    {
        initW();
        for (int e = 0; e < episodeCount; e++) {
            List<RLExperience> episode = env.generateEpisode(policy);
            updateValues(episode);
        }

        if (functionRenderer!= null)
            functionRenderer.render(env,vFunction,featureExtractor,w);
    }

    private void initW() {
        w = new double[featureExtractor.featureCount(false)];
    }

    public static void main(String[] args) {
        RandomWalkLong rwl = new RandomWalkLong(1000,500);
        GradientMonteCarloPrediction mcp = new GradientMonteCarloPrediction(new RWFeatureExtractor(1000,100),new LinearFunction(10),0.00001);

        mcp.setFunctionRenderer(new RWAFRenderer("valuePlot.jpg"));
        mcp.run(rwl,new RWUniformRandomPolicy(),1000000);

    }
}
