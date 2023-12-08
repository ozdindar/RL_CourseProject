package rl.methods.dyna;

import com.google.common.collect.HashBasedTable;
import org.apache.commons.math3.util.Pair;
import rl.MapBasedPolicy;
import rl.SimpleRLExperience;
import rl.base.*;
import rl.gui.DynaListener;

import java.security.SecureRandom;
import java.util.*;

public class TabularDynaQ implements ControlAgent{

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

    public TabularDynaQ(double alpha, double gamma, double epsilon, int n) {
        this.alpha = alpha;
        this.gamma = gamma;
        this.epsilon = epsilon;
        this.n = n;
        model = new SimpleDeterministicModel();

        listeners = new ArrayList<>();
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

    public RLPolicy run(RLEnvironment env,int episodeCount)
    {
        initQ(env);
        stepCount =0;

        for (int i = 0; i<episodeCount; i++) {
            currentState = env.reset() ;

            while(!env.isOver())
            {
                RLExperience experience = directRL(env);
                notifyListeners(DynaEvent.DirectRLFinished,currentState);
                stepCount++;
                model.learn(experience);
                if (i>0)
                    plan(env,n);
                currentState = experience.getNextState();
            }

        }

        RLPolicy policy = buildPolicy(env);

        printResults(env,policy,episodeCount);
        return policy;
    }

    @Override
    public String name() {
        return "TabularDynaQ";
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

    private void plan(RLEnvironment env,int n) {
         Set<RLState> states = model.getExperiencedStates();
        for (int i = 0; i < n; i++) {
            RLState state = randomItem(states);
            Set<RLAction> actions = model.getExperiencedActions(state);
            RLAction action = randomItem(actions);
            RLExperience experience = model.getExperience(state,action);

            double q = getQValue(env, state,action);

            RLAction nextAction = greedySelection(env,experience.getNextState(),env.getActions(experience.getNextState()));
            double maxQ= getQValue(env,experience.getNextState(),nextAction);
            double value = q + alpha*( experience.getReward() + gamma*maxQ -q);

            qTable.put(state,action,value);
        }
    }

    private void notifyListeners(DynaEvent event,RLState state) {
        if(listeners.isEmpty())
            return;
        for (DynaListener listener:listeners)
            listener.dynaEvent(event,qTable,model,state);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
        Pair<RLState,Double> next = env.step(action);
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

        double value = q + alpha*( r + gamma*maxQ -q);
        qTable.put(state,action,value);
        return new SimpleRLExperience(state,action,nextState,r);
    }

    private void initQ(RLEnvironment env) {
        qTable = HashBasedTable.create();


    }
}
