package rl.methods.dyna;

import com.google.common.collect.HashBasedTable;
import org.apache.commons.math3.util.Pair;
import rl.MapBasedPolicy;
import rl.SimpleRLExperience;
import rl.base.*;
import rl.gui.DynaListener;
import rl.util.Triplet;

import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class TabularDynaQ_PS implements ControlAgent {

    private static final Double TERMINAL_Q = 0.0;
    private static final Double INITIAL_Q = 0.0;

    Random rng = new SecureRandom();

    double alpha = 0.1;
    double gamma = 1.0;
    private double epsilon;

    DeterministicModel model;
    HashBasedTable<RLState, RLAction,Double> qTable;
    private int n;

    private RLPolicyRenderer policyRenderer;

    RLState currentState;

    List<DynaListener> listeners;
    private long stepCount;

    PriorityBlockingQueue<Triplet<RLState,RLAction,Double>> pQueue;
    private double theta;

    public TabularDynaQ_PS(double alpha, double gamma, double epsilon, int n) {
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.n = n;
        model = new SimpleDeterministicModel();

        listeners = new ArrayList<>();
        pQueue = new PriorityBlockingQueue<>(1000,(x,y)->Double.compare(x.third(),y.third()));
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public void addListener(DynaListener listener)
    {
        listeners.add(listener);
    }

    public void setPolicyRenderer(RLPolicyRenderer policyRenderer) {
        this.policyRenderer = policyRenderer;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public void setEpsilon(double epsilon) {
        this.epsilon = epsilon;
    }

    public void setN(int n) {
        this.n = n;
    }

    public RLPolicy run(RLEnvironment env, int episodeCount)  {
        initQ(env);
        stepCount =0;

        for (int i = 0; i<episodeCount; i++) {
            env.reset();
            while(!env.isOver())
            {
                RLExperience experience = directRL(env);
                RLState state = env.currentState();
                notifyListeners(DynaEvent.DirectRLFinished,state);
                stepCount++;
                model.learn(experience);
                plan(env,n);
            }

        }

        RLPolicy policy = buildPolicy(env);

        printResults(env,policy,episodeCount);
        return policy;
    }

    @Override
    public String name() {
        return "TabularDynaQ_PS";
    }


    private void printResults(RLEnvironment env, RLPolicy policy, int episodeCount) {
        if (policyRenderer!= null)
            policyRenderer.renderPolicy(env, policy);

        System.out.println();
        System.out.println("Avg step count = "+ ((double)stepCount/(double) episodeCount));
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
        /*todo*/
        return policy;
    }

     <T> T randomItem(Set<T> set)
     {
         int index = rng.nextInt(set.size());
         Iterator<T> iter = set.iterator();
         for (int i = 0; i < index; i++) {
             iter.next();
         }
         return iter.next();
     }

    private void plan(RLEnvironment env,int n)  {
         Set<RLState> states = model.getExperiencedStates();
        for (int i = 0; i < n && !pQueue.isEmpty(); i++) {

            Triplet<RLState,RLAction,Double> sa = null;
            try {
                sa = pQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e.getMessage());
            }

            RLState state = sa.first();
            RLAction action = sa.second();

            RLExperience experience = model.getExperience(state,action);

            double q = getQValue(env, state,action);

            RLAction nextAction = greedySelection(env,experience.getNextState(),env.getActions(experience.getNextState()));
            double maxQ= getQValue(env,experience.getNextState(),nextAction);
            double tdError = ( experience.getReward() + gamma*maxQ -q);
            double value = q + alpha*tdError;

            qTable.put(state,action,value);
            updatePredecessors(env,state);
        }
    }

    private void updatePredecessors(RLEnvironment env,RLState state) {
        List<Pair<RLState,RLAction>> predecessors =  model.getPredecessors(state);

        for (Pair<RLState,RLAction> sa:predecessors)
        {
            RLState s = sa.getFirst();
            RLAction a = sa.getSecond();
            Double r = model.getExperience(s,a).getReward();
            RLAction bestAction = greedySelection(env,state,env.getActions(state));

            double error = Math.abs(r+ gamma*getQValue(env,state,bestAction)-getQValue(env,s,a) );
            if (error> theta)
                pQueue.add(Triplet.with(s,a,error));
        }
    }

    private void notifyListeners(DynaEvent event,RLState state) {
        if (listeners.isEmpty())
            return;
        for (DynaListener listener:listeners)
            listener.dynaEvent(event,qTable,model,state);

    }

    private RLAction greedySelection(RLEnvironment env,RLState s, List<? extends RLAction> actions) {
        Collections.shuffle(actions);
        RLAction bestAction =actions.stream().max((a1,a2)->Double.compare(getQValue(env,s,a1),getQValue(env,s,a2))).get();
        return bestAction;
    }

    private RLAction chooseEpsilonGreedyAction(RLEnvironment env, RLState s, double epsilon) {

        List<? extends RLAction> actions = env.getActions(s);

        if (rng.nextDouble()<epsilon)
        {
            return actions.get(rng.nextInt(actions.size()));
        }

        return greedySelection(env,s,actions);
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

    private RLExperience directRL(RLEnvironment env) {
        RLState state = env.currentState();
        RLAction action = chooseEpsilonGreedyAction(env, state,epsilon);
        Pair<RLState, Double> next = env.step(action);
        double r = next.getSecond();
        RLState nextState =  next.getFirst(); // Assuming non-stochastic action

        double maxQ=0.0;
        if (env.isTerminal(nextState))
            maxQ=TERMINAL_Q;
        else {
            RLAction nextAction = greedySelection(env,nextState, env.getActions(nextState));
            maxQ= getQValue(env,nextState,nextAction);
        }

        double q = getQValue(env, state,action);

        double tdError = ( r + gamma*maxQ -q);
        if (tdError>0) {
            double value = q + alpha * tdError;
            qTable.put(state, action, value);
        }
        if (Math.abs(tdError) > theta)
            pQueue.add(Triplet.with(state,action,Math.abs(tdError)));
        return new SimpleRLExperience(state,action,nextState,r);
    }

    private void initQ(RLEnvironment env) {
        qTable = HashBasedTable.create();


    }
}
