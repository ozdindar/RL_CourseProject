package rl.tasks.grid;

import org.apache.commons.math3.util.Pair;
import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLState;
import rl.gui.DynaGridPanel;
import rl.methods.dyna.TabularDynaQ_PS;

import javax.swing.*;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Grid2D implements RLEnvironment {

    private static final double DEFAULT_REWARD = 0.0;
    private static final double TERMINAL_REWARD = 5.0;
    protected int rowCount;
    protected int colCount;
    protected Grid2DState[][] states;

    public Grid2DState getInitialState() {
        return initialState;
    }

    protected Grid2DState initialState;

    RLState currentState;

    public Grid2D(int rowCount, int colCount) {
        this.rowCount = rowCount;
        this.colCount = colCount;
        buildStates();
    }


    private void buildStates() {
        states = new Grid2DState[rowCount][colCount];

        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < colCount; c++) {
                states[r][c] = new Grid2DState(r, c);
            }
        }
    }

    public void setTerminal(int r, int c) {
        states[r][c].setTerminal(true);
    }

    public void setObstacle(int r, int c) {
        states[r][c].setObstacle(true);
    }

/*
   public List<RLState> getStates() {
        return Arrays.stream(states).flatMap(Arrays::stream).collect(Collectors.toList());
    }*/

    @Override
    public RLState currentState() {
        return currentState;
    }

    @Override
    public RLState reset() {
        if (initialState != null)
            currentState = initialState;
        else {
            Random r = new SecureRandom();
            currentState = states[r.nextInt(rowCount)][r.nextInt(colCount)];
        }
        return currentState;
    }

    @Override
    public Pair<RLState, Double> step(RLAction action) {
        Grid2DState gs = (Grid2DState) currentState;
        GridDirection direction = (GridDirection) action;

        double reward = DEFAULT_REWARD;

        int nextRow = gs.row + direction.getRowDelta();
        int nextCol = gs.col + direction.getColDelta();

        nextRow = Math.max(nextRow, 0);
        nextCol = Math.max(nextCol, 0);

        nextRow = Math.min(rowCount - 1, nextRow);
        nextCol = Math.min(colCount - 1, nextCol);

        Grid2DState nextState = getState(nextRow,nextCol);
        if (!nextState.isObstacle()) {
            currentState = nextState;
            if (nextState.isTerminal())
                reward = TERMINAL_REWARD;
            return Pair.create(nextState, reward);
        }
        else return Pair.create(currentState,0.0);
    }


    @Override
    public List<? extends RLAction> getActions(RLState state) {
        return Arrays.asList(GridDirection.Up,GridDirection.Down,GridDirection.Left,GridDirection.Right);
    }

    @Override
    public boolean isTerminal(RLState state) {
        Grid2DState grid2DState = (Grid2DState) state;
        return grid2DState.isTerminal();
    }


    public Grid2DState getState(int r, int c) {
        return states[r][c];
    }

    public void setInitial(int row, int col) {
        initialState = getState(row, col);
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColCount() {
        return colCount ;
    }

    public static void main(String[] args) throws InterruptedException {
        Grid2D g = new Grid2D(6, 9);
        g.setTerminal(5, 5);
        g.setInitial(2, 0);

        g.setObstacle(1,7);
        g.setObstacle(2,7);
        g.setObstacle(3,7);


        g.setObstacle(1,2);
        g.setObstacle(2,2);
        g.setObstacle(3,2);


        g.setObstacle(4,5);


        JFrame main= new JFrame("Game of Life");

        DynaGridPanel panel = new DynaGridPanel(g);

        panel.setVisibleQValues(true);
        main.add(panel);

        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        main.setResizable(false);
        main.setSize(800,800);

        main.setVisible(true);

        TabularDynaQ_PS dynaQ = new TabularDynaQ_PS(0.1,0.95,0.1,100);

        dynaQ.setTheta(0.01);

        dynaQ.setPolicyRenderer(new Grid2DPolicyRenderer());
        dynaQ.addListener(panel);
        dynaQ.run(g,10000);
    }


}

