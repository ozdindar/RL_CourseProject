package rl.tasks.pathfinding;

import rl.base.RLAction;
import rl.base.RLState;

import java.util.ArrayList;
import java.util.List;

public class GraphState implements RLState {
    int id;
    List<RLAction> actions= new ArrayList<>();


    public GraphState(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        return id == ((GraphState)obj).id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }


    void addAction(RLAction action)
    {
        actions.add(action);
    }


    public List<? extends RLAction> getActions() {
        return actions;
    }





    @Override
    public String toString() {
        return "["+ id+ "]";
    }

    public void clearActions() {
        actions.clear();
    }
}
