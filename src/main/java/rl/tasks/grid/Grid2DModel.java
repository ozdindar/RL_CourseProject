package rl.tasks.grid;

import rl.SimpleTransition;
import rl.base.EnvironmentModel;
import rl.base.RLAction;
import rl.base.RLState;
import rl.base.RLTransition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Grid2DModel implements EnvironmentModel {

    int rowCount;
    int colCount;
    TabularGrid2DState[][] states;

    public Grid2DModel(int rowCount, int colCount) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        buildStates();
    }

    private void buildStates() {
        states = new TabularGrid2DState[rowCount][colCount];

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                states[r][c]= new TabularGrid2DState(r,c);
            }
        }
        buildTransitions();
    }

    private void buildTransitions() {
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                states[r][c].createActions(this);
            }
        }
    }

    public void setTerminal(int r, int c, double reward)
    {
        states[r][c].setTerminal(true,reward);
        buildTransitions();
    }

    public void setObstacle(int r,int c)
    {
        states[r][c].setObstacle(true);
        if (r>0)
            states[r-1][c].createActions(this);
        if (c>0)
            states[r][c-1].createActions(this);
        if (r<rowCount-1)
            states[r+1][c].createActions(this);
        if (c<colCount-1)
            states[r][c+1].createActions(this);
    }


    @Override
    public List<RLState> getStates() {
        return Arrays.stream(states).flatMap(Arrays::stream).collect(Collectors.toList());
    }

    /*
    @Override
    public RLState reset() {
        Random r = new SecureRandom();
        return states[r.nextInt(rowCount)][r.nextInt(colCount)];
    }*/

    /*
    @Override
    public Pair<RLState, Double> step(RLState state, RLAction action) {
        GridAction ga = (GridAction) action;
        TabularGrid2DState gs= (TabularGrid2DState) getTransitions(state,action).get(0).to();
        return Pair.of(gs,gs.stateReward());
    }*/

    @Override
    public List<? extends RLTransition> getTransitions(RLState state, RLAction action) {
        GridDirection direction = (GridDirection) action;
        switch (direction)
        {
            case Up :  return upTransition(state);
            case Down: return downTransition(state);
            case Left: return leftTransition(state);
            case Right: return rightTransition(state);
        };
        return null;
    }

    private List<? extends RLTransition> rightTransition(RLState state) {
        TabularGrid2DState grid2DState = (TabularGrid2DState) state;
        int row =grid2DState.row;
        int col = grid2DState.col;
        List<RLTransition> transitions = new ArrayList<>();
        if (col>=colCount-1 || getState(row,col+1).isObstacle())
            transitions.add(new SimpleTransition(state,state, 1, grid2DState.stateReward));
        else transitions.add(new SimpleTransition(state,getState(row,col+1), 1,getState(row,col+1).stateReward));

        return transitions;
    }

    private List<? extends RLTransition> leftTransition(RLState state) {
        TabularGrid2DState grid2DState = (TabularGrid2DState) state;
        int row =grid2DState.row;
        int col = grid2DState.col;
        List<RLTransition> transitions = new ArrayList<>();
        if (col==0 || getState(row,col-1).isObstacle())
            transitions.add(new SimpleTransition(state,state, 1,grid2DState.stateReward));
        else transitions.add(new SimpleTransition(state,getState(row,col-1), 1,getState(row,col-1).stateReward));

        return transitions;
    }

    private List<? extends RLTransition> downTransition(RLState state) {
        TabularGrid2DState grid2DState = (TabularGrid2DState) state;
        int row =grid2DState.row;
        int col = grid2DState.col;
        List<RLTransition> transitions = new ArrayList<>();
        if (row>=rowCount-1 || getState(row+1,col).isObstacle())
            transitions.add(new SimpleTransition(state,state, 1,grid2DState.stateReward));
        else transitions.add(new SimpleTransition(state,getState(row+1,col), 1,getState(row+1,col).stateReward));


        return transitions;
    }

    private List<? extends RLTransition> upTransition(RLState state) {
        TabularGrid2DState grid2DState = (TabularGrid2DState) state;
        int row =grid2DState.row;
        int col = grid2DState.col;
        List<RLTransition> transitions = new ArrayList<>();
        if (row==0 || getState(row-1,col).isObstacle())
            transitions.add(new SimpleTransition(state,state, 1,grid2DState.stateReward));
        else transitions.add(new SimpleTransition(state,getState(row-1,col),1,getState(row-1,col).stateReward));

        return transitions;
    }

    @Override
    public List<? extends RLAction> getActions(RLState state) {
        TabularGrid2DState grid2DState = (TabularGrid2DState) state;
        return grid2DState.getActions();
    }

    @Override
    public boolean isTerminal(RLState state) {
        TabularGrid2DState grid2DState = (TabularGrid2DState) state;
        return grid2DState.isTerminal();
    }




    public TabularGrid2DState getState(int r, int c) {
        return states[r][c];
    }
}
