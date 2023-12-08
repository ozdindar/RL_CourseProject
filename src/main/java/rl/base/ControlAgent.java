package rl.base;

public interface ControlAgent {
    RLPolicy run(RLEnvironment environment, int episodeCount);
    String name();
}
