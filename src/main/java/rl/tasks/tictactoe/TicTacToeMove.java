package rl.tasks.tictactoe;


import rl.base.RLAction;

public class TicTacToeMove  implements RLAction {
    int row;
    int col;


    public TicTacToeMove(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }


    @Override
    public String name() {
        return row+ "-" + col;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public int hashCode() {
        return name().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        TicTacToeMove move = (TicTacToeMove) obj;
        return row==move.row && col == move.col;
    }
}
