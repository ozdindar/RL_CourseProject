package rl.tasks.rod;

import rl.base.RLState;

import java.util.Objects;

public class RodState implements RLState {

    int x;
    int y;
    int angle;

    public RodState(int x, int y, int angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "["+ x*RodManeuvering.UNIT_LENGTH +"-"+
                    y*RodManeuvering.UNIT_LENGTH +" "+
                    angle*RodManeuvering.UNIT_ANGLE+ "]"  ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RodState rodState = (RodState) o;
        return x == rodState.x && y == rodState.y && angle == rodState.angle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, angle);
    }
}
