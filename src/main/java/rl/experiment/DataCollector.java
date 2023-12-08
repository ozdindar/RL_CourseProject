package rl.experiment;

import org.apache.commons.math3.util.Pair;
import rl.base.EnvironmentObserver;
import rl.base.RLEnvironment;
import rl.base.RLState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCollector implements EnvironmentObserver {

    protected Trial curTrial;



    /**
     * contains all trial data for each agent
     */
    protected Map<String, List<Trial>> agentTrials;

    /**
     * The name of the current agent being tested
     */
    protected String curAgentName;

    public DataCollector() {
        agentTrials = new HashMap<>();
    }

    public Map<String, List<Trial>> getAgentTrials() {
        return agentTrials;
    }

    void endTrial()
    {
        agentTrials.get(curAgentName).add(curTrial);
    }

    void startNewTrial(String agent)
    {
        curTrial = new Trial();

        curAgentName = agent;
        if (!agentTrials.containsKey(agent))
            agentTrials.put(agent,new ArrayList<>());
    }


    synchronized public void endEpisode(){
        if (curTrial!= null)
            this.curTrial.setupForNewEpisode();
    }

    @Override
    public void environmentReset(RLEnvironment environment) {
        endEpisode();
    }

    @Override
    public void environmentStep(Pair<RLState, Double> result) {
        curTrial.stepIncrement(result.getSecond());
    }
}
