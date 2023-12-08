package rl.tasks.tictactoe;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public class RandomTTTPlayer implements TicTacToePlayer{
    Random rng= new SecureRandom();

    @Override
    public TicTacToeMove getMove(TicTacToeBoard board) {
        List<TicTacToeMove> moves = TicTacToeUtil.getMoves(board.cells);
        return moves.get(rng.nextInt(moves.size()));
    }
}
