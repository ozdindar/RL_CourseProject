package rl.gui;

import com.google.common.collect.HashBasedTable;
import rl.base.RLAction;
import rl.base.RLState;
import rl.methods.dyna.DeterministicModel;
import rl.methods.dyna.DynaEvent;

public interface DynaListener {
    void dynaEvent(DynaEvent event, HashBasedTable<RLState, RLAction, Double> qTable, DeterministicModel model, RLState state);
}
