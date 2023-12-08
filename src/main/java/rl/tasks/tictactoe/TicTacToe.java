package rl.tasks.tictactoe;

import org.apache.commons.math3.util.Pair;
import rl.base.DeterministicPolicy;
import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLState;
import rl.methods.temporaldifference.QLearning;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.Scanner;




public class TicTacToe implements RLEnvironment {

    public static final int STATE_INDEX = 0;
    public static final int PLAYER = 0;
    private static final int BOARD_INDEX = 1;
    public static final double LOST_REWARD = -10;
    public static final double WIN_REWARD = 10;
    public static final double NO_REWARD = 0;

    TicTacToePlayer opponent;


    int boardSize;
    int player;
    RLState currentState;

    public TicTacToe(int boardSize, int player, TicTacToePlayer opponent) {
        this.opponent = opponent;
        this.boardSize = boardSize;
        this.player = player;
    }

    @Override
    public Pair<RLState, Double> step(RLAction action)
    {
        TicTacToeBoard current = (TicTacToeBoard) currentState;
        TicTacToeMove move = (TicTacToeMove) action;

        TicTacToeBoard nextBoard = new TicTacToeBoard(current,move);
        double reward = NO_REWARD;

        currentState = nextBoard;
        if (isGameOver(nextBoard) && ! TicTacToeUtil.isDraw(nextBoard.cells)) {
            reward = WIN_REWARD;
            return Pair.create(nextBoard,reward);
        }
        TicTacToeMove opponentMove = opponent.getMove(nextBoard);
        nextBoard.makeMove(opponentMove);

        if (isGameOver(nextBoard)&& !TicTacToeUtil.isDraw(nextBoard.cells)) {
            reward = LOST_REWARD;
        }

        return Pair.create(nextBoard,reward);
    }


    @Override
    public List<? extends RLAction> getActions(RLState state) {
        TicTacToeBoard board = (TicTacToeBoard) state;
        return TicTacToeUtil.getMoves(board.cells);
    }

    @Override
    public boolean isTerminal(RLState state) {
        TicTacToeBoard board = (TicTacToeBoard) state;
        return isGameOver(board);
    }


    @Override
    public RLState currentState() {
        return currentState;
    }

    @Override
    public RLState reset() {
        Random r = new SecureRandom();

        TicTacToeBoard tttb = new TicTacToeBoard(boardSize);
        int mc = r.nextInt(5);

        boolean boardCreated= false;
        while (!boardCreated) {
            for (int i = 0; i < mc || tttb.currentPlayer!=player; i++) {
                if (isGameOver(tttb))
                    break;
                makeRandomMove(r, tttb);
            }
            if (!isGameOver(tttb))
                boardCreated= true;
        }

        currentState = tttb;
        return currentState;
    }



    public static void makeRandomMove(Random r, TicTacToeBoard tttb) {
        List<TicTacToeMove> moves = TicTacToeUtil.getMoves(tttb.cells);
        TicTacToeMove m = moves.get(r.nextInt(moves.size()));

       tttb.makeMove(m);
    }



    public static void main(String[] args) {

        int boardSize = 4;
        TicTacToe ttt = new TicTacToe(boardSize,0,new RandomTTTPlayer());
        QLearning qLearning = new QLearning(0.1,null);


        DeterministicPolicy policy = (DeterministicPolicy) qLearning.run(ttt, 10000000);

        RLPolicyPlayer player = new TicTacToePolicyPlayer(policy);

        playTicTacToe(player,boardSize,10);
    }

    private static void playTicTacToe(RLPolicyPlayer player, int boardSize,int roundCount) {
        for (int i = 0; i < roundCount; i++) {
            TicTacToeBoard board = new TicTacToeBoard(boardSize);

            Scanner scanner = new Scanner(System.in);
            while (!isGameOver(board))
            {
                TicTacToeMove move = (TicTacToeMove) player.getAction(board);
                move = move!= null ? move : TicTacToeUtil.getMoves(board.cells).get(0);
                board.makeMove(move);
                System.out.println(board);
                if (isGameOver(board)) {
                    System.out.println("YOU LOST!");
                    break;
                }
                System.out.println("Please make a move:");

                int r = scanner.nextInt();
                int c = scanner.nextInt();
                TicTacToeMove m = new TicTacToeMove(r,c);
                board.makeMove(m);
            }
        }
    }

    public static boolean isGameOver(TicTacToeBoard board) {
        return TicTacToeUtil.isGameOver(board.cells);
    }
}
