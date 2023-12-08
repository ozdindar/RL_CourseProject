package rl.tasks.tictactoe;

import rl.base.RLState;

import java.util.List;

public class TicTacToeBoard implements RLState {

    public static final int Empty = 0;
    public static final int Cross = 1;
    public static final int Circle =2;

    long key;
    int cells[][];

    private double reward;
    private boolean isTerminal;
    private List<TicTacToeMove> actions;

    int boardSize;
    int currentPlayer =0;

    @Override
    public int hashCode() {
        return Long.hashCode(key);
    }

    @Override
    public boolean equals(Object obj) {
        TicTacToeBoard other = (TicTacToeBoard) obj;
        return key == other.key;
    }

    private void createActions() {
        actions= TicTacToeUtil.getMoves(cells);

    }

    public TicTacToeBoard(int boardSize) {
        this.boardSize = boardSize;
        cells = TicTacToeUtil.initialBoard(boardSize);
        isTerminal = false;

        key = TicTacToeUtil.zobristKey(cells);
        reward = TicTacToe.NO_REWARD;
        createActions();
    }

    public TicTacToeBoard(TicTacToeBoard current, TicTacToeMove move) {
        this.boardSize = current.boardSize;
        cells = new int[boardSize][boardSize];
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                cells[r][c] = current.cells[r][c];
            }
        }
        isTerminal = false;
        currentPlayer = current.currentPlayer;

        makeMove(move);
    }

    public void makeMove(TicTacToeMove m) {
        cells[m.row][m.col] = TicTacToeUtil.playerSymbol(currentPlayer);
        currentPlayer = (currentPlayer+1)%2;

        key = TicTacToeUtil.updateKey(key,m,currentPlayer);

        isTerminal = TicTacToeUtil.isGameOver(cells);
        if (isTerminal)
            reward = (currentPlayer == TicTacToe.PLAYER) ? TicTacToe.LOST_REWARD:TicTacToe.WIN_REWARD;
        createActions();
    }

    public boolean isGameOver()
    {
        return TicTacToeUtil.isGameOver(cells);
    }


    public boolean isValid(TicTacToeMove m) {
        return cells[m.getRow()][m.getCol()]== Empty;
    }



    public long getKey() {

        return key;
    }

    public int[][] getBoard() {
        return cells;
    }


    public String toString()
    {
        String st ="";

        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                if (cells[r][c]!= Empty)
                    st += cells[r][c]== Cross ? "X":"O";
                else st += " ";
                if (c<boardSize-1)
                    st+="|";
            }
            if (r<boardSize-1)
                st+="\n";
        }

        st +=  "\n"+key;
        return st;
    }

    public static void main(String[] args) {
        TicTacToeBoard tttb= new TicTacToeBoard(3);
        TicTacToeBoard tttb2= new TicTacToeBoard(3);
        TicTacToeMove ticTacToeMove1 = new TicTacToeMove(1,1);
        TicTacToeMove ticTacToeMove2 = new TicTacToeMove(2,2);
        TicTacToeMove ticTacToeMove3 = new TicTacToeMove(0,0);

        System.out.println(tttb);
        System.out.println(tttb2);
    }


    public int[][] cloneCell() {

        return new int[0][];
    }
}


