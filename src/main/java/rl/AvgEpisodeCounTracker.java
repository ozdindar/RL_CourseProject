package rl;

import org.apache.commons.math3.util.Pair;
import rl.base.EnvironmentObserver;
import rl.base.RLEnvironment;
import rl.base.RLState;


public class AvgEpisodeCounTracker implements EnvironmentObserver {

    long currentEpisodeLength;
    long episodeCount;

    double averageEpisodeLength;

    public long getCurrentEpisodeLength() {
        return currentEpisodeLength;
    }

    public long getEpisodeCount() {
        return episodeCount;
    }

    public double getAverageEpisodeLength() {
        return averageEpisodeLength;
    }

    @Override
    public void environmentReset(RLEnvironment environment) {
        episodeCount++;
        averageEpisodeLength += (double) (currentEpisodeLength-averageEpisodeLength)/(double) episodeCount;
        System.out.println(this);
        currentEpisodeLength=0;

    }

    @Override
    public void environmentStep(Pair<RLState, Double> result) {
        currentEpisodeLength++;
    }

    @Override
    public String toString() {
        return "EC:"+ episodeCount+ " Avg. Ep. Len: "+ averageEpisodeLength+ "  Cur.Ep:"+ currentEpisodeLength;
    }
}
