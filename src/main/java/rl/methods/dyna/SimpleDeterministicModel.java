package rl.methods.dyna;

import com.google.common.collect.HashBasedTable;
import org.apache.commons.math3.util.Pair;
import rl.base.RLAction;
import rl.base.RLExperience;
import rl.base.RLState;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SimpleDeterministicModel implements DeterministicModel {

    HashBasedTable<RLState, RLAction, RLExperience> memory;

    public SimpleDeterministicModel() {
        memory = HashBasedTable.create();
    }

    @Override
    public void learn(RLExperience experience) {
        memory.put(experience.getState(),experience.getAction(),experience);
    }

    public Set<RLState> getExperiencedStates(){
        return memory.rowKeySet();
    }

    public Set<RLAction> getExperiencedActions(RLState state){
        return memory.row(state).keySet();
    }

    @Override
    public RLExperience getExperience(RLState state, RLAction action) {
        return memory.get(state,action);
    }

    @Override
    public List<Pair<RLState, RLAction>> getPredecessors(RLState state) {

        List<Pair<RLState,RLAction> >predecessors= memory.cellSet().stream().filter((c)->c.getValue().getNextState()==state).
                map((c)->new Pair<RLState,RLAction>(c.getRowKey(),c.getColumnKey())).
                collect(Collectors.toList());

        return predecessors;
    }
}
