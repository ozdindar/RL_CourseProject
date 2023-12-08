package rl.tasks.grid;

import rl.base.RLAction;
import rl.base.RLState;

import java.util.Arrays;
import java.util.List;

public class TabularGrid2DState implements RLState {
    private static final double DEFAULT_REWARD = -1;
    int row;
    int col;
    List<RLAction> actions;
    boolean obstacle;
    boolean terminal;

    double stateReward;

    @Override
    public int hashCode() {
        return (row+"-"+ col).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TabularGrid2DState))
            return false;
        TabularGrid2DState gobj = (TabularGrid2DState) obj;
        return (row== gobj.row )&& (col== gobj.col);
    }

    public void setStateReward(double stateReward) {
        this.stateReward = stateReward;
    }




    public TabularGrid2DState(int r, int c) {
        row = r;
        col = c;
        obstacle = false;
        terminal = false;
        stateReward = DEFAULT_REWARD;

    }


    void createActions(Grid2DModel grid) {
        actions = Arrays.asList(GridDirection.Up,GridDirection.Down,GridDirection.Left,GridDirection.Right);
    }

    @Override
    public String toString() {
        return "["+row+"-"+col+"] " + stateReward;
    }


    public void setTerminal(boolean terminal, double reward) {
        this.terminal = terminal;
        this.stateReward= reward;
    }

    public void setObstacle(boolean abstacle) {
        this.obstacle = abstacle;
        stateReward =0;
        terminal = true;
    }

    public boolean isTerminal() {
        return terminal;
    }

    public double stateReward() {
        return stateReward;
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


    public List<? extends RLAction> getActions() {
        return actions;
    }
}
