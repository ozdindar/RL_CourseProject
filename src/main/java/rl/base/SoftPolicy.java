package rl.base;

import com.google.common.collect.HashBasedTable;

public interface SoftPolicy extends RLPolicy {

    void setActionProbability(RLState s, RLAction action, double prob);
    HashBasedTable<RLState,RLAction,Double> getActionProbs();
}
