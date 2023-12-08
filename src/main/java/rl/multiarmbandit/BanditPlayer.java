package rl.multiarmbandit;

public interface BanditPlayer {
    int getAction();
    void getReward(int a, double bandit);
    void init(int armCount);

    void printStats();

    String getName();
}
