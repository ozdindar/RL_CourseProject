package rl.tasks.randomwalk;

import rl.base.RLAction;

import java.util.Objects;

public class RWAction  implements RLAction {

    public final static RWAction LeftAction = new RWAction(RWDirection.Left);
    public final static RWAction RightAction = new RWAction(RWDirection.Right);

    RWDirection dir;

    public RWAction(RWDirection dir) {
        this.dir = dir;
    }

    @Override
    public String name() {
        return dir.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RWAction rwAction = (RWAction) o;
        return dir == rwAction.dir;
    }

    @Override
    public int hashCode() {
        return Objects.hash(dir);
    }

    @Override
    public String toString() {
        return name();
    }

    public RWDirection getDir() {
        return dir;
    }
}
