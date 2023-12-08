package rl;

import rl.base.RLState;
import rl.base.ValueFunction;

import java.util.Map;
import java.util.Set;

public class ValueMap implements ValueFunction {

    private Map<RLState, Double> map;

    public ValueMap(Map<RLState, Double> map) {
        this.map = map;
    }

    @Override
    public Set<RLState> getStates() {
        return map.keySet();
    }


    @Override
    public double getValue(RLState s) {
        return map.get(s);
    }
}
