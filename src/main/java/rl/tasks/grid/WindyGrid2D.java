package rl.tasks.grid;

import org.apache.commons.math3.util.Pair;
import rl.base.RLAction;
import rl.base.RLPolicy;
import rl.base.RLState;
import rl.methods.temporaldifference.QLearning;
import rl.methods.temporaldifference.SarsaEpsilon;

public class WindyGrid2D extends Grid2D {
    int winds[];

    public WindyGrid2D(int rowCount, int colCount, int winds[]) {
        super(rowCount,colCount);
        this.winds = winds;
     }

    @Override
    public Pair<RLState, Double> step(RLAction action) {
        Grid2DState gs = (Grid2DState) currentState();
        GridDirection dir = (GridDirection) action;

        double reward = -1;

        int nextRow = gs.getRow() + dir.getRowDelta()- winds[gs.getCol()];
        int nextCol = gs.getCol() + dir.getColDelta();

        nextRow = Math.max(nextRow, 0);
        nextCol = Math.max(nextCol, 0);

        nextRow = Math.min(rowCount - 1, nextRow);
        nextCol = Math.min(colCount - 1, nextCol);

        Grid2DState next = getState(nextRow,nextCol);
        currentState = next;

        if (isTerminal(next))
            reward =0;

        return Pair.create(next,reward);
    }

    public static void main(String[] args) {
        WindyGrid2D wg = new WindyGrid2D(7, 10, new int[]{0, 0, 0, 1, 1, 1, 2, 2, 1, 0});
        wg.setTerminal(3, 7);
        wg.setInitial(3, 0);

        QLearning qLearning = new QLearning(0.1, new Grid2DPolicyRenderer());

        SarsaEpsilon sarsaEpsilon = new SarsaEpsilon(0.1,0.5,null);

        RLPolicy policy = sarsaEpsilon.run(wg, 100000);
    }
}


