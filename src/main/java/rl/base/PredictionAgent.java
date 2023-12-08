package rl.base;

public interface PredictionAgent {
    ValueFunction run(RLEnvironment environment,int episodeCount);
}
