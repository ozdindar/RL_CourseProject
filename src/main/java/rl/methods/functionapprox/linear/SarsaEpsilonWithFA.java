package rl.methods.functionapprox.linear;

import org.apache.commons.math3.util.Pair;
import rl.AvgEpisodeCounTracker;
import rl.EnvironmentServer;
import rl.PolicySimulator;
import rl.base.*;
import rl.tasks.randomwalk.RWAction;
import rl.tasks.randomwalk.RandomWalkLong;
import rl.tasks.rod.RMFeatureExtractor;
import rl.tasks.rod.RodManeuvering;
import rl.tasks.rod.RodPanel;
import rl.tasks.rod.RodState;

import javax.swing.*;
import java.awt.*;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SarsaEpsilonWithFA implements ControlAgent {
    static final double INITIAL_V =0.5;
    private static final Double TERMINAL_V = 0.0;

    Random rng = new SecureRandom();

    double alpha = 0.1;
    final double gamma = 1.0;

    double epsilon;

    FeatureExtractor featureExtractor;
    ApproximationFunction qFunction;


    RLPolicyRenderer policyRenderer;

    public SarsaEpsilonWithFA(double epsilon, double alpha, FeatureExtractor featureExtractor, ApproximationFunction qFunction, RLPolicyRenderer policyRenderer) {
        this.policyRenderer = policyRenderer;
        this.alpha = alpha;
        this.epsilon= epsilon;
        this.featureExtractor=featureExtractor;
        this.qFunction = qFunction;
    }

    private double getQValue(RLEnvironment env, RLState s, RLAction a) {
        double[] sa_features= featureExtractor.featuresOf(s,a);
        return qFunction.value(sa_features);
    }

    private RLAction greedySelection(RLEnvironment env,RLState s, List<? extends RLAction> actions) {

        Collections.shuffle(actions);
        RLAction bestAction =actions.stream().max((a1,a2)->Double.compare(getQValue(env,s,a1),getQValue(env,s,a2))).get();
        return bestAction;
    }


    private RLAction chooseEpsilonGreedyAction(RLEnvironment env, RLState s,double epsilon) {

        List<? extends RLAction> actions = env.getActions(s);

        if (rng.nextDouble()<epsilon)
        {
            return actions.get(rng.nextInt(actions.size()));
        }

        return greedySelection(env,s,actions);
    }

    double calculateTDError(RLEnvironment env, RLState s, RLAction a)
    {
        Pair<RLState,Double> next = env.step(a);
        double reward = next.getSecond();
        RLState nextState = next.getFirst();
        double qNextSA=0.0;
        RLAction na=null;
        if (env.isTerminal(nextState))
            qNextSA=TERMINAL_V;
        else {
            na = chooseEpsilonGreedyAction(env, nextState, epsilon);
            qNextSA =getQValue(env,nextState,na);
        }
        double qSA= getQValue(env,s,a);
        return  (reward + gamma*qNextSA- qSA);
    }

    public RLPolicy run(RLEnvironment env, int episodeCount)
    {
        qFunction.initWeights();
        for (int e = 0; e < episodeCount; e++) {
            RLState s = env.reset();
            while (!env.isTerminal(s))
            {
                RLAction a = chooseEpsilonGreedyAction(env,s,epsilon);
                double error = calculateTDError(env,s, a);
                updateW(s,a,error);
                s = env.currentState();
            }
        }

        RLPolicy policy = buildPolicy(env);

        if (policyRenderer!= null)
            policyRenderer.renderPolicy(policy);

        return policy;
    }

    private void updateW(RLState s, RLAction a,double error) {
        double[] sa_features = featureExtractor.featuresOf(s,a);
        qFunction.updateWeights(sa_features,error,alpha);
    }


    @Override
    public String name() {
        return "Sarsa";
    }

    private RLPolicy buildPolicy(RLEnvironment env) {
        return new QFunctionPolicy(featureExtractor,qFunction,env);
    }




    static void demoRandomWalk()
    {
        RandomWalkLong rwl = new RandomWalkLong(1000,499);
        ApproximationFunction qFunction = new SimpleNeuralNetwork(10,20,1);
        ApproximationFunction lqFunction = new LinearFunction(10);
        SarsaEpsilonWithFA sarsa = new SarsaEpsilonWithFA(0.1,0.1,new RWFeatureExtractor(1000,100),qFunction, null);

        RLPolicy policy = sarsa.run(rwl,100000);
        RWAction a = (RWAction) policy.getAction(rwl.getState(499));

        PolicySimulator simulator = new PolicySimulator(rwl,policy,1000);
        simulator.setPrintSimulation(true);
        simulator.simulate(1);
        System.out.println(a);
    }

    public static RodManeuvering buildRodEnvironment()
    {
        RodState initial = new RodState(3,15,9);
        RodState terminal = new RodState(30,20,0);

        RodManeuvering rm = new RodManeuvering(400,400,initial,terminal);

        rm.addObstacle(new Polygon(new int[]{120,200,250,170},new int[]{20,80,60,30},4));

        rm.addObstacle(new Polygon(new int[]{150,370,370,200,200,150},new int[]{120,120,150,150,370,370},6));

        rm.addObstacle(new Polygon(new int[]{260,360,360,260},new int[]{240,240,280,280},4));

        return rm;
    }

    static void demoRodManeuvering()
    {
        RodManeuvering rm = buildRodEnvironment();
        RodPanel panel = new RodPanel(rm);
        JFrame main =new JFrame("Rod MAneuvering");
        main.add(panel);
        main.setSize(1200,800);
        main.setVisible(true);
        EnvironmentServer es = new EnvironmentServer(rm,panel, new AvgEpisodeCounTracker());
        ApproximationFunction qFunction = new SimpleNeuralNetwork(4,20,1);
        ApproximationFunction lqFunction = new LinearFunction(10);
        SarsaEpsilonWithFA sarsa = new SarsaEpsilonWithFA(0.1,0.1,new RMFeatureExtractor(),qFunction, null);

        RLPolicy policy = sarsa.run(es,1000);


        PolicySimulator simulator = new PolicySimulator(es,policy,1000);
        simulator.simulate(1);

    }

    public static void main(String[] args) {
        demoRodManeuvering();


    }
}
