package rl.tasks.tictactoe;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToeUtil {


    static long zobristValues[][][];




    public static List<TicTacToeMove> getMoves(int[][] cells) {
        List<TicTacToeMove> moves = new ArrayList<>();

        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells.length; c++) {
                if (cells[r][c]== TicTacToeBoard.Empty)
                    moves.add(new TicTacToeMove(r,c));
            }
        }
        return moves;
    }

    public static boolean isGameOver(int[][] cells) {
       return hasPerfectRow(cells)|| hasPerfectCol(cells)|| hasPerfectDiagonal(cells)|| isFull(cells);
    }

    public static boolean isDraw(int[][] cells) {
        return isFull(cells) && !( hasPerfectRow(cells)|| hasPerfectCol(cells)|| hasPerfectDiagonal(cells)) ;
    }

    private static boolean isFull(int[][] cells) {
        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                if (cells[r][c]== TicTacToeBoard.Empty)
                    return false;
            }
        }
        return true;
    }

    private static boolean hasPerfectDiagonal(int[][] cells) {
        return hasPerfectDiagonalOf(cells,TicTacToeBoard.Cross) || hasPerfectDiagonalOf(cells,TicTacToeBoard.Circle) ;
    }

    public static boolean hasPerfectDiagonalOf(int[][] cells, int symbol) {
        boolean perfectDiagonal= true;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][i]!= symbol)
            {
                perfectDiagonal = false;
                break;
            }
        }
        if (perfectDiagonal)
            return true;
        perfectDiagonal = true;
        for (int i = 0; i < cells.length; i++) {
            if (cells[i][cells.length-i-1]!= symbol)
            {
                perfectDiagonal = false;
                break;
            }
        }
        return perfectDiagonal;
    }

    private static boolean hasPerfectCol(int[][] cells) {
        return hasPerfectColOf(cells,TicTacToeBoard.Cross) || hasPerfectColOf(cells, TicTacToeBoard.Circle) ;
    }

    public static boolean hasPerfectColOf(int[][] cells, int symbol) {
        boolean perfectCol = false;
        for (int c = 0; c<cells[0].length ; c++) {
            perfectCol = true;
            for (int r = 0; r < cells.length; r++) {
                if (cells[r][c] != symbol) {
                    perfectCol = false;
                    break;
                }
            }
            if (perfectCol)
                return true;
        }
        return false;
    }

    private static boolean hasPerfectRow(int[][] cells) {
        return hasPerfectRowOf(cells,TicTacToeBoard.Cross) || hasPerfectRowOf(cells, TicTacToeBoard.Circle) ;
    }

    public static boolean hasPerfectRowOf(int[][] cells, int symbol) {

        boolean perfectRow = false;
        for (int r = 0; r <cells.length ; r++) {
            perfectRow = true;
            for (int c = 0; c < cells[r].length; c++) {
                if (cells[r][c] != symbol) {
                    perfectRow = false;
                    break;
                }
            }
            if (perfectRow)
                return true;
        }
        return false;
    }

    public static int playerSymbol(int currentPlayer) {
        return currentPlayer==0 ?
                TicTacToeBoard.Cross: TicTacToeBoard.Circle;
    }

    public static int[][] initialBoard(int boardSize) {
        int cells[][] = new int[boardSize][boardSize];
        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c <boardSize; c++) {
                cells[r][c] = TicTacToeBoard.Empty;
            }
        }
        return cells;
    }

    public static int otherPlayer(int player) {
        return (player+1)%2;
    }





    public static long zobristKey(int[][] cells) {
        if (zobristValues == null)
            initializeZobrist(cells.length);

        long key = 0;

        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                key ^= zobristValues[r][c][cells[r][c]];
            }
        }

        return key;
    }

    public static long zobristKey(int[][] cells, TicTacToeMove m,  int player) {
        if (zobristValues == null)
            initializeZobrist(cells.length);

        long key = 0;

        for (int r = 0; r < cells.length; r++) {
            for (int c = 0; c < cells[r].length; c++) {
                if (r==m.getRow() && c==m.col)
                    key ^= zobristValues[r][c][TicTacToeUtil.playerSymbol(player)];
                else key ^= zobristValues[r][c][cells[r][c]];
            }
        }

        return key;
    }

    static Random rng = new SecureRandom();

    private static void initializeZobrist(int boardSize) {

        zobristValues = new long[boardSize][boardSize][3];


        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                for (int i = 0; i < 3; i++) {
                    zobristValues[r][c][i] = rng.nextLong();
                }
            }
        }
    }

    public static long updateKey(long key, TicTacToeMove m, int player) {
        key ^= zobristValues[m.getRow()][m.getCol()][TicTacToeBoard.Empty];
        key ^= zobristValues[m.getRow()][m.getCol()][playerSymbol(player)];
        return key;
    }
}
