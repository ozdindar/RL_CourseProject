package rl.tasks.pathfinding;

import rl.base.RLAction;

public class GraphAction implements RLAction {
    int from;
    int to;




    public GraphAction(int from, int to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof GraphAction))
            return false;
        return from== ((GraphAction) obj).from && to==((GraphAction) obj).to;
    }

    @Override
    public String name() {
        return from+ "-"+ to;
    }


    @Override
    public String toString() {
        return "("+ from+ "->" + to + ")";
    }
}
