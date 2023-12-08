package rl.experiment;

import java.util.List;

public interface CollectedData {
    List<Double> averageEpisodeReward();

    List<Double> cumulativeStepEpisode();
}
