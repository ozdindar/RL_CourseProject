package rl.tasks.tictactoe;

import rl.base.RLAction;
import rl.base.RLState;

public interface RLPolicyPlayer {
    RLAction getAction(RLState state);
}
