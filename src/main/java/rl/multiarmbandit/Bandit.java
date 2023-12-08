package rl.multiarmbandit;

public interface Bandit {

    void reset();
    double roll(int arm);
    int armCount();
    void print();
}
