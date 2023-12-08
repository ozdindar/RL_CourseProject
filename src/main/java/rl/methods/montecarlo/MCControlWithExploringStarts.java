package rl.methods.montecarlo;

import com.google.common.collect.HashBasedTable;
import org.apache.commons.math3.util.Pair;
import rl.MapBasedPolicy;
import rl.SimpleRLExperience;
import rl.base.*;
import rl.tasks.blackjack.BlackJack;
import rl.tasks.blackjack.BlackJackPolicyRenderer;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MCControlWithExploringStarts implements ControlAgent{

    static final double INITIAL_V =0;
    private static final double DISCOUNT_RATE = 1.0;

    HashBasedTable<RLState,RLAction,Double> qTable;
    HashBasedTable<RLState,RLAction,Integer> steps;

    DeterministicPolicy policy;

    RLPolicyRenderer policyRenderer;

    public MCControlWithExploringStarts( RLPolicyRenderer policyRenderer ) {
        //this.agent = new MCAgent();

        this.policyRenderer = policyRenderer;
    }

    @Override
    public RLPolicy run(RLEnvironment env,int episodeCount)
    {
        qTable = HashBasedTable.create();
        steps = HashBasedTable.create();
        policy = new MapBasedPolicy();

        for (int e = 0; e < episodeCount; e++) {
            List<RLExperience> episode = generateEpisode(env);
            updateQTable(episode);
        }

        if (policyRenderer != null)
            policyRenderer.renderPolicy(env,policy);

        return policy;
    }

    @Override
    public String name() {
        return "MonteCarlo Expl.Starts";
    }

    private void updateQTable(List<RLExperience> episode) {
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
            updatePolicy(s,a,newV);
        }
    }

    private void updatePolicy(RLState s, RLAction a, double newV) {
        Map<RLAction,Double> actionMap = qTable.row(s);

        double best= -1*Double.MAX_VALUE;
        RLAction bestAction = null;
        for (RLAction action:actionMap.keySet())
        {
            if (actionMap.get(action)>best)
            {
                best = actionMap.get(action);
                bestAction = action;
            }
        }
        policy.setAction(s,bestAction);
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

    private List<RLExperience> generateEpisode(RLEnvironment env) {

        List<RLExperience> episode = new ArrayList<>();
        env.reset();
        RLState s = env.currentState();
        RLAction a = env.getActions().get(new SecureRandom().nextInt(env.getActions().size()));
        Pair<RLState,Double> obs = env.step(a);
        double reward = obs.getSecond();
        episode.add(new SimpleRLExperience(s,a,obs.getFirst(),reward));
        while (!env.isOver())
        {
            s = env.currentState();
            a = getPolicyAction(env,s);
            obs = env.step(a);
            reward = obs.getSecond();
            episode.add(new SimpleRLExperience(s,a,obs.getFirst(),reward));
        }

        return episode;
    }

    private RLAction getPolicyAction(RLEnvironment env,RLState s) {
        if (policy.containsActionFor(s))
            return policy.getAction(s);
        else {
            return env.getActions().get(new SecureRandom().nextInt(env.getActions().size()));
        }
    }

    public static void main(String[] args) {
        MCControlWithExploringStarts mcp = new MCControlWithExploringStarts(new BlackJackPolicyRenderer());

        mcp.run(new BlackJack(),100000000);


    }
}
