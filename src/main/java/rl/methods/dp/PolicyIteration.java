package rl.methods.dp;


import rl.base.*;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class PolicyIteration implements DynamicProgramming {

    final double GAMMA = 0.99;
    final double EPSILON = 1e-7;
    final double INITIAL_V = 0.0;

    HashMap<RLState,Double> v;
    HashMap<RLState,Double> vNext;

    DeterministicPolicy pi;


    private RLValueRenderer valueRenderer;
    private RLPolicyRenderer policyRenderer;


    public PolicyIteration(DeterministicPolicy pi) {
        this.pi = pi;
    }

    public void setValueRenderer(RLValueRenderer valueRenderer) {
        this.valueRenderer = valueRenderer;
    }

    public void setPolicyRenderer(RLPolicyRenderer policyRenderer) {
        this.policyRenderer = policyRenderer;
    }

    private void init(EnvironmentModel environment) {
        vNext = new HashMap<>();
        v = new HashMap<>();

        for (RLState state:environment.getStates())
            vNext.put(state,INITIAL_V);

        for (RLState state:environment.getStates())
            pi.setAction(state,environment.getActions(state).get(0));
    }

    public void perform(EnvironmentModel environment, int iteration)
    {
        init(environment);
        long epis0de=1;
        do {
            policyEvaluation(environment,iteration);
            System.out.println("<<"+ (epis0de++)+ ">>");
        }
        while (policyIteration(environment));
        printResults(environment);
    }

    private void printResults(EnvironmentModel model) {
        if (valueRenderer != null)
            valueRenderer.renderValues(model,v);

        if (policyRenderer != null)
            policyRenderer.renderPolicy(model,pi);
    }

    private boolean policyIteration(EnvironmentModel environment) {
        boolean policy_stable = true;

        for (RLState state:environment.getStates())
        {
            RLAction a = pi.getAction(state);
            RLAction best = bestAction(environment,state);

            if (a!= best) {
                policy_stable = false;
                pi.setAction(state,best);
            }
        }
        return policy_stable;
    }

    private int maxIndex(List<Double> list) {
        int maxIndex= 0;

        for (int i = 1; i <list.size() ; i++) {
            if (list.get(i)>list.get(maxIndex))
            {
                maxIndex=i;
            }
        }
        return maxIndex;
    }

    private RLAction bestAction(EnvironmentModel environment, RLState state) {

        return environment.getActions(state).stream().
                max(Comparator.comparingDouble((a)->actionValue(environment,state,a))).get();
        /*
        List<Double> actionValues=new ArrayList<>();
        for (RLAction action:state.getActions())
        {
            actionValues.add(actionValue(action));
        }
        int best = maxIndex(actionValues);
        return state.getActions().get(best);*/
    }

    private void policyEvaluation(EnvironmentModel environment, int iteration) {
        double delta =0;

        for (int n = 0; n < iteration; n++) {
            v.putAll(vNext);
            delta = 0;

            for (RLState state: environment.getStates())
            {
                update(environment,state);
                double err = Math.abs(vNext.get(state)-v.get(state));
                if (err>delta)
                    delta=err;
            }
            if (delta<EPSILON)
                break;
        }
    }

    double actionValue(EnvironmentModel environment, RLState state, RLAction action)
    {
        double val = 0;
        List<? extends RLTransition> transitions = environment.getTransitions(state,action);
        for(RLTransition transition: transitions)
        {
            val +=  transition.prob()*transition.reward()+ transition.prob()*v.get(transition.to());
        }
        return val;
    }

    private void update(EnvironmentModel environment, RLState state) {
        double newValue = 0;
        if (!environment.isTerminal(state)) {
            double actionValue = actionValue(environment, state, pi.getAction(state));
            newValue += GAMMA*actionValue;
        }
        vNext.put(state,newValue);
    }




}
