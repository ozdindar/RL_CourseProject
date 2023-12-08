package rl.tasks.grid;

import rl.base.RLAction;

public enum GridDirection implements RLAction {
    Up("U",-1,0),Down("D",1,0),Left("L",0,-1),Right("R",0,1);

    GridDirection(String shortName, int rowDelta, int colDelta) {
        this.shortName = shortName;
        this.rowDelta = rowDelta;
        this.colDelta = colDelta;
    }

    String shortName;
    int rowDelta;
    int colDelta;

    String shortName(){
        return shortName;
    }

    public int getRowDelta() {
        return rowDelta;
    }

    public int getColDelta() {
        return colDelta;
    }
}
