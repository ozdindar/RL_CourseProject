package rl.methods.montecarlo;

import com.google.common.collect.HashBasedTable;
import rl.base.*;
import rl.tasks.blackjack.BlackJack;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class MCControlWithEpsilonSoftPolicy implements ControlAgent {

    static final double INITIAL_V =0;
    private static final double DISCOUNT_RATE = 1.0;
    private final double epsilon;

    HashBasedTable<RLState, RLAction,Double> qTable;
    HashBasedTable<RLState,RLAction,Integer> steps;


    SoftPolicy policy;

    RLSoftPolicyRenderer policyRenderer;

    public MCControlWithEpsilonSoftPolicy( double epsilon, RLSoftPolicyRenderer policyRenderer) {
        this.epsilon = epsilon;
        this.policyRenderer = policyRenderer;
    }

    @Override
    public RLPolicy run(RLEnvironment env, int episodeCount)
    {
        policy = new TableBasedSoftPolicy(env);
        qTable = HashBasedTable.create();
        steps = HashBasedTable.create();

        for (int e = 0; e < episodeCount; e++) {
            List<RLExperience> episode = env.generateEpisode(policy);
            updateQTable(env,episode);
        }

        if (policyRenderer != null)
            policyRenderer.renderPolicy(policy);

        return policy;
    }

    @Override
    public String name() {
        return "MonteCarlo E-Soft";
    }

    private void updateQTable(RLEnvironment env, List<RLExperience> episode) {
        double returns =0;
        for (int i = episode.size()-1; i>=0; i--) {
            RLExperience step = episode.get(i);
            RLState s= step.getState();
            RLAction a = step.getAction();
            double qValue = getQValue(s,a);
            returns = returns*DISCOUNT_RATE +  step.getReward();
            int stepCount = steps.get(s,a)+1;
            double newV = qValue+ (1.0/stepCount)*(returns-qValue);

            qTable.put(s,a,newV);
            steps.put(s,a,stepCount);
            updatePolicy(env,s,a,newV);
        }
    }

    private void updatePolicy(RLEnvironment env, RLState s, RLAction a, double newV) {
        Map<RLAction,Double> actionMap = qTable.row(s);

        RLAction bestAction = actionMap.entrySet().stream().max(Comparator.comparingDouble(Map.Entry::getValue)).get().getKey();

        List<? extends RLAction> actions = env.getActions(s);
        policy.setActionProbability(s,bestAction,1-epsilon + epsilon/actions.size());
        for (RLAction action : actions)
        {
            if (action != bestAction)
                policy.setActionProbability(s,action,epsilon/actions.size());
        }
    }


    private double getQValue(RLState s,RLAction a) {
        if (qTable.contains(s,a))
            return qTable.get(s,a);
        else
        {
            qTable.put(s,a,INITIAL_V);
            steps.put(s,a,0);
            return INITIAL_V;
        }
    }




    public static void main(String[] args) {
        MCControlWithEpsilonSoftPolicy mcp = new MCControlWithEpsilonSoftPolicy(0.6,new GenericSoftPolicyRenderer());

        mcp.run(new BlackJack(),1000000);


    }

}
