package rl;

import org.apache.commons.math3.util.Pair;
import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLPolicy;
import rl.base.RLState;
import rl.util.ThreadUtil;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class PolicySimulator  {

    Random rng = new SecureRandom();

    RLEnvironment env;
    RLPolicy policy;

    int maxStepPerEpisode;

    long pause=0;



    boolean printSimulation;

    public PolicySimulator(RLEnvironment env, RLPolicy policy, int maxStepPerEpisode) {
        this.env = env;
        this.policy = policy;
        this.maxStepPerEpisode = maxStepPerEpisode;
    }

    public void setPrintSimulation(boolean printSimulation) {
        this.printSimulation = printSimulation;
    }

    public void setPause(long pause)
    {
        this.pause = pause;
    }
    public double simulateAnEpisode() {
        double totalReward =0;
        RLState s = env.reset();
        int stepCount =0;

        while(!env.isOver() && stepCount<maxStepPerEpisode)
        {
            RLAction action = policy.getAction(s);
            if (action == null)
                action = randomAction(s);

            Pair<RLState,Double> obs =  env.step(action);
            totalReward += obs.getSecond();
            if (printSimulation)
                System.out.println("["+stepCount+"]  S:"+ s + " A:"+ action+ " R:"+ obs.getSecond());
            if (pause>0)
                ThreadUtil.pause(pause);
            s = env.currentState();
            stepCount++;
        }
        return totalReward;
    }

    public double simulate(int episodeCount)
    {
        if(episodeCount==0)
            return 0;

        double totalReward = 0;

        for (int i = 0; i < episodeCount; i++) {
            totalReward += simulateAnEpisode();
        }

        return totalReward/episodeCount;
    }

    private RLAction randomAction(RLState s) {
        List<? extends RLAction> actions = env.getActions(s);
        return actions.get(rng.nextInt(actions.size()));
    }
}
