package rl.tasks.randomwalk;

import rl.base.RLState;

public class RWState implements RLState {
    int index;


    public RWState(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RWState))
            return false;
        return index == ((RWState)obj).index;
    }

    @Override
    public String toString() {
        return "["+ index+"]";
    }
}
