package rl.methods.temporaldifference;

import com.google.common.collect.HashBasedTable;
import org.apache.commons.math3.util.Pair;
import rl.DeterministicPolicyRenderer;
import rl.MapBasedPolicy;
import rl.base.*;
import rl.tasks.pathfinding.Graph;
import rl.tasks.pathfinding.PathFinding;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ExpectedSarsa  implements ControlAgent{
    static final double INITIAL_V =0.5;
    private static final Double TERMINAL_V = 0.0;

    Random rng = new SecureRandom();

    double alpha = 0.1;
    final double gamma = 1.0;

    double epsilon;

    HashBasedTable<RLState, RLAction,Double> qTable;



    RLPolicyRenderer policyRenderer;

    public ExpectedSarsa(double epsilon, double alpha, RLPolicyRenderer policyRenderer) {
        this.policyRenderer = policyRenderer;
        this.alpha = alpha;
        this.epsilon= epsilon;
    }

    private double getQValue(RLEnvironment env, RLState s, RLAction a) {

        if (qTable.contains(s,a))
            return qTable.get(s,a);
        else
        {
            if (env.isTerminal(s))
                qTable.put(s,a,TERMINAL_V);
            else qTable.put(s,a,INITIAL_V);
            return qTable.get(s,a);
        }
    }

    public RLPolicy run(RLEnvironment env, int episodeCount)
    {
        qTable = HashBasedTable.create();

        for (int e = 0; e < episodeCount; e++) {
            RLState s = env.reset();
            while (!env.isTerminal(s))
            {
                RLAction a = chooseEpsilonGreedyAction(env,s,epsilon);
                Pair<RLState,Double> next = env.step(a);
                double reward = next.getSecond();
                RLState nextState = next.getFirst();
                double qNextSA=0.0;
                if (env.isTerminal(nextState))
                    qNextSA=TERMINAL_V;
                else {
                    //RLAction nextAction = chooseEpsilonGreedyAction(env, nextState, epsilon);
                    qNextSA =expectedQValue(env,nextState);
                }
                double qSA= getQValue(env,s,a);
                double nextQSA = qSA + alpha*(reward + gamma*qNextSA- qSA);
                qTable.put(s,a,nextQSA);
                s = nextState;
            }
        }

        RLPolicy policy = buildPolicy(env);

        if (policyRenderer!= null)
            policyRenderer.renderPolicy(policy);

        return policy;
    }

    @Override
    public String name() {
        return "ExpectedSarsa";
    }

    private double expectedQValue(RLEnvironment env, RLState nextState) {
        List<? extends RLAction> actions = env.getActions(nextState);
        RLAction greedyAction = greedySelection(env,nextState,actions);
        double qValue =0;

        for (RLAction action:actions)
        {
            double prob = (action==greedyAction) ? (1-epsilon): epsilon/actions.size();
            qValue += prob*getQValue(env,nextState,action);
        }
        return qValue;
    }

    private RLPolicy buildPolicy(RLEnvironment env) {
        DeterministicPolicy policy = new MapBasedPolicy();

        for (RLState state:qTable.rowKeySet())
        {
            if (env.isTerminal(state))
                continue;
            List<? extends RLAction> actions = env.getActions(state);
            RLAction greedyAction = greedySelection(env,state,actions);
            policy.setAction(state,greedyAction);
        }
        return policy;
    }

    private RLAction chooseEpsilonGreedyAction(RLEnvironment env, RLState s,double epsilon) {

        List<? extends RLAction> actions = env.getActions(s);

        if (rng.nextDouble()<epsilon)
        {
            return actions.get(rng.nextInt(actions.size()));
        }

        return greedySelection(env,s,actions);
    }

    private RLAction greedySelection(RLEnvironment env,RLState s, List<? extends RLAction> actions) {

        RLAction bestAction =actions.stream().max((a1,a2)->Double.compare(getQValue(env,s,a1),getQValue(env,s,a2))).get();
        return bestAction;
    }

    static void demoPathFinding()
    {
        Graph<Integer> graph = new Graph<>();
        graph.addVertices(Arrays.asList(0,1,2,3,4,5));
        graph.successors(0).add(4);
        graph.successors(1).addAll(Arrays.asList(3,5));
        graph.successors(2).addAll(Arrays.asList(3));
        graph.successors(3).addAll(Arrays.asList(1,2,4));
        graph.successors(4).addAll(Arrays.asList(0,3,5));

        PathFinding pathFinding = new PathFinding(graph);
        pathFinding.setTerminal(5);

        ExpectedSarsa sarsa = new ExpectedSarsa(0.1,0.5,new DeterministicPolicyRenderer());

        sarsa.run(pathFinding,1000);
    }

    public static void main(String[] args) {
        demoPathFinding();


    }
}
