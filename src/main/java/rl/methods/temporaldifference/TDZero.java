package rl.methods.temporaldifference;

import org.apache.commons.math3.util.Pair;
import rl.ValueMap;
import rl.base.*;
import rl.methods.montecarlo.RandomWalkValueRenderer;
import rl.tasks.randomwalk.RWUniformRandomPolicy;
import rl.tasks.randomwalk.RandomWalk;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TDZero implements PredictionAgent{
    static final double INITIAL_V =0.5;
    private static final Double TERMINAL_V = 0.0;

    Random rng = new SecureRandom();

    final double alpha = 0.1;
    final double gamma = 1.0;

    Map<RLState,Double> vTable;

    RLPolicy policy;

    RLValueRenderer valueRenderer;

    public TDZero(RLPolicy policy, RLValueRenderer valueRenderer) {
        this.policy = policy;
        this.valueRenderer = valueRenderer;
    }

    private double getV(RLEnvironment env,RLState s) {

        if (vTable.containsKey(s))
            return vTable.get(s);
        else
        {
            if (env.isTerminal(s))
                vTable.put(s,TERMINAL_V);
            else vTable.put(s,INITIAL_V);
            return vTable.get(s);
        }
    }

    public ValueFunction run(RLEnvironment env, int episodeCount)
    {
        vTable = new HashMap<>();

        for (int e = 0; e < episodeCount; e++) {
            RLState s = env.reset();
            while (!env.isTerminal(s))
            {
                RLAction a = policy.getAction(s);
                Pair<RLState,Double> next = env.step(a);
                RLState nextState = next.getFirst();
                double reward = next.getSecond();
                double v= getV(env,s);
                double nextV = v + alpha*(reward + gamma*getV(env,nextState)- v);
                vTable.put(s,nextV);
                s = nextState;
            }
        }

        if (valueRenderer!= null)
            valueRenderer.renderValues(vTable);

        return new ValueMap(vTable);
    }

    public static void main(String[] args) {
        TDZero tdz = new TDZero(new RWUniformRandomPolicy(),new RandomWalkValueRenderer());

        tdz.run(new RandomWalk(7,3),100000);
    }
}
