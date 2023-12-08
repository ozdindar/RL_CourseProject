package rl.methods.temporaldifference;

import com.google.common.collect.HashBasedTable;
import org.apache.commons.math3.util.Pair;
import rl.MapBasedPolicy;
import rl.base.*;
import rl.tasks.grid.Grid2D;
import rl.tasks.grid.Grid2DPolicyRenderer;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class QLearning implements ControlAgent {

    private static final Double TERMINAL_Q = 0.0;
    private static final double TERMINAL_V = 0.0;
    Random rng = new SecureRandom();

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    double alpha = 0.1;
    final double gamma = 1.0;

    double epsilon;

    private static final Double INITIAL_Q = 0.0;

    HashBasedTable<RLState, RLAction,Double> qTable;
    private RLPolicyRenderer policyRenderer;

    public QLearning(double epsilon, RLPolicyRenderer policyRenderer) {
        this.epsilon = epsilon;
        this.policyRenderer = policyRenderer;
    }

    public void setPauseInterval(long pauseInterval) {
        this.pauseInterval = pauseInterval;
    }

    long pauseInterval;

    void pause(long millis)
    {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private RLAction chooseEpsilonGreedyAction(RLEnvironment env, RLState s, double epsilon) {

        List<? extends RLAction> actions = env.getActions(s);

        if (rng.nextDouble()<epsilon)
        {
            return actions.get(rng.nextInt(actions.size()));
        }

        return greedySelection(env,s,actions);
    }

    private RLAction greedySelection(RLEnvironment env,RLState s, List<? extends RLAction> actions) {
        Collections.shuffle(actions);
        RLAction bestAction =actions.stream().max((a1,a2)->Double.compare(getQValue(env,s,a1),getQValue(env,s,a2))).get();
        return bestAction;
    }

    public RLPolicy run(RLEnvironment env, int episodeCount)
    {
        initQ(env);

        for (int e = 0; e < episodeCount; e++) {

            RLState state= env.reset() ;

            while (!env.isOver())
            {
                RLAction action = chooseEpsilonGreedyAction(env,state,epsilon);
                Pair<RLState,Double> next = env.step(action);
                double r = next.getSecond();
                RLState nextState =  next.getFirst(); // Assuming non-stochastic action

                double maxQ=0.0;
                if (env.isTerminal(nextState))
                    maxQ=TERMINAL_V;
                else {
                    RLAction nextAction = greedySelection(env,nextState, env.getActions(nextState));
                    maxQ= getQValue(env,nextState,nextAction);
                }

                double q = getQValue(env,state,action);

                double value = q + alpha*( r + gamma*maxQ -q);
                qTable.put(state,action,value);
                state= nextState;

                if (pauseInterval>0)
                    pause(pauseInterval);

                if (e>500000)
                    debugMode();
            }

        }

        DeterministicPolicy policy = buildPolicy(env);

        printResults(env,policy);

        return policy;
    }

    @Override
    public String name() {
        return "QLearning";
    }

    private void debugMode() {
        pauseInterval=100;
        epsilon=0.95;
    }

    private void printResults(RLEnvironment env, RLPolicy policy) {
        if (policyRenderer!= null)
            policyRenderer.renderPolicy(env, policy);
    }

    private DeterministicPolicy buildPolicy(RLEnvironment env) {
        DeterministicPolicy policy = new MapBasedPolicy();

        for (RLState state:qTable.rowKeySet())
        {
            if (env.isTerminal(state))
                continue;
            List<? extends RLAction> actions = env.getActions(state);
            RLAction greedyAction = greedySelection(env,state,actions);
            policy.setAction(state,greedyAction);
        }
        /*todo*/
        return policy;
    }



    private double getQValue(RLEnvironment env,RLState s,RLAction a) {

        if (qTable.contains(s,a))
            return qTable.get(s,a);
        else
        {
            if (env.isTerminal(s))
                qTable.put(s,a,TERMINAL_Q);
            else qTable.put(s,a,INITIAL_Q);
            return qTable.get(s,a);
        }
    }

    private void initQ(RLEnvironment env) {
        qTable = HashBasedTable.create();
    }

    public void setPolicyRenderer(RLPolicyRenderer policyRenderer) {
        this.policyRenderer = policyRenderer;
    }

    public static void main(String[] args) {
        Grid2D grid2D = new Grid2D(4,4);
        grid2D.setTerminal(0,3);
        grid2D.setTerminal(1,3);
        grid2D.setObstacle(1,1);
        grid2D.setObstacle(2,1);

        QLearning qLearning = new QLearning(0.1,new Grid2DPolicyRenderer());
        qLearning.setPolicyRenderer(new Grid2DPolicyRenderer());


        qLearning.run(grid2D, 1000);
    }



}
