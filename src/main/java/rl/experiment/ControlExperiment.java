package rl.experiment;

import rl.EnvironmentServer;
import rl.base.ControlAgent;
import rl.base.RLEnvironment;

import java.util.List;
import java.util.Map;

public class ControlExperiment {

    public static Map<String,List<Trial>> perform(RLEnvironment environment, List<ControlAgent> agents, int trialCount, int episodeCount )
    {
        DataCollector dataCollector = new DataCollector();
        EnvironmentServer server = new EnvironmentServer(environment,dataCollector);

        for (ControlAgent agent:agents)
        {
            for (int t = 0; t < trialCount; t++) {
                dataCollector.startNewTrial(agent.name());
                agent.run(server,episodeCount);
                dataCollector.endTrial();
            }
        }
        return dataCollector.getAgentTrials();
    }



}
