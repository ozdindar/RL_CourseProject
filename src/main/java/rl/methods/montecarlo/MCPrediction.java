package rl.methods.montecarlo;

import rl.ValueMap;
import rl.base.*;
import rl.tasks.blackjack.BlackJack;
import rl.tasks.blackjack.BlackJackPolicy;
import rl.tasks.blackjack.BlackJackValueRenderer;
import rl.tasks.randomwalk.RWUniformRandomPolicy;
import rl.tasks.randomwalk.RandomWalk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MCPrediction implements PredictionAgent{

    static final double INITIAL_V =0.5;
    private static final double DISCOUNT_RATE = 1.0;

    Map<RLState,Double> vTable;
    Map<RLState,Integer> steps;

    RLPolicy policy;

    RLValueRenderer valueRenderer;

    boolean everyVisitCounted;

    public MCPrediction( RLPolicy policy, RLValueRenderer valueRenderer) {
        this.policy = policy;
        this.valueRenderer = valueRenderer;
    }

    public void setEveryVisitCounted(boolean everyVisitCounted) {
        this.everyVisitCounted = everyVisitCounted;
    }

    @Override
    public ValueFunction run(RLEnvironment environment, int episodeCount)
    {
        vTable = new HashMap<>();
        steps = new HashMap<>();
        for (int e = 0; e < episodeCount; e++) {
            List<RLExperience> episode = environment.generateEpisode(policy);
            updateValues(episode);
        }

        if (valueRenderer != null)
            valueRenderer.renderValues(vTable);
        return new ValueMap(vTable);
    }

    private void updateValues(List<RLExperience> episode) {
        double returns =0;
        for (int i = episode.size()-1; i>=0; i--) {
            RLExperience step = episode.get(i);
            RLState s= step.getState();
            returns = returns*DISCOUNT_RATE +  step.getReward();
            if (everyVisitCounted|| episode.stream().limit(i).noneMatch((x) -> x.getState() == s))
            {
                updateVTable(returns, s);
            }
        }
    }

    private void updateVTable(double returns, RLState s) {
        double v = getV(s);
        int stepCount = steps.get(s)+1;
        double newV = v+ (1.0/stepCount)*(returns -v);

        vTable.put(s,newV);
        steps.put(s,stepCount);
    }

    private double getV(RLState s) {
        if (vTable.containsKey(s))
            return vTable.get(s);
        else
        {
            vTable.put(s,INITIAL_V);
            steps.put(s,1);
            return INITIAL_V;
        }
    }

    static void blackjackDemo()
    {
        MCPrediction mcp = new MCPrediction(new BlackJackPolicy(),new BlackJackValueRenderer());
        mcp.run(new BlackJack(),1000000);
    }

    static void randomWalkDemo()
    {
        MCPrediction mcp = new MCPrediction(new RWUniformRandomPolicy(),new RandomWalkValueRenderer());

        mcp.run(new RandomWalk(7,3),100000);
    }

    public static void main(String[] args) {
        randomWalkDemo();
    }
}
