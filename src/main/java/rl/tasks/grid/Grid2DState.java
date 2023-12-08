package rl.tasks.grid;

import rl.base.RLState;

public class Grid2DState implements RLState {

    int row;
    int col;
    boolean obstacle;
    boolean terminal;

    public Grid2DState(int r, int c) {
        row = r;
        col = c;
        obstacle = false;
        terminal = false;
    }

    @Override
    public String toString() {
        return "["+row+"-"+col+"] " ;
    }


    public void setTerminal(boolean terminal) {
        this.terminal = terminal;
    }

    public void setObstacle(boolean abstacle) {
        this.obstacle = abstacle;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public int hashCode() {
        return (row+"-"+ col).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Grid2DState))
            return false;
        Grid2DState gobj = (Grid2DState) obj;
        return (row== gobj.row )&& (col== gobj.col);
    }

}
