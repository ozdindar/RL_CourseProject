package rl.methods.montecarlo;

import com.google.common.collect.HashBasedTable;
import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLState;
import rl.base.SoftPolicy;

import java.security.SecureRandom;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class TableBasedSoftPolicy implements SoftPolicy {

    HashBasedTable<RLState,RLAction,Double> actionProbs;
    private Random rng = new SecureRandom();
    RLEnvironment environment;

    public TableBasedSoftPolicy(RLEnvironment environment)
    {
        actionProbs= HashBasedTable.create();
        this.environment = environment;
    }


    @Override
    public void setActionProbability(RLState s, RLAction a, double p) {
        actionProbs.put(s,a,p);
    }

    @Override
    public RLAction getAction( RLState s) {

        if (!actionProbs.containsRow(s))
        {
            createRow(environment,s);
        }

        double d = rng.nextDouble();
        double total =0;
        Map<RLAction,Double> actionmap = actionProbs.row(s);
        for (RLAction action:actionmap.keySet())
        {
            total += actionmap.get(action);
            if (total>=d)
                return action;
        }
        return null;
    }

    @Override
    public HashBasedTable<RLState, RLAction, Double> getActionProbs() {
        return actionProbs;
    }

    private void createRow(RLEnvironment env, RLState s) {
        List<?extends RLAction> actions = env.getActions(s);
        for (RLAction a:actions)
        {
            actionProbs.put(s,a,1.0/actions.size());
        }
    }
}
