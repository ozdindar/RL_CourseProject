package rl.tasks.tictactoe;

import rl.base.DeterministicPolicy;
import rl.base.RLAction;
import rl.base.RLState;

public class TicTacToePolicyPlayer implements RLPolicyPlayer {

    DeterministicPolicy policy;

    public TicTacToePolicyPlayer(DeterministicPolicy policy) {
        this.policy = policy;
    }

    @Override
    public RLAction getAction(RLState state)
    {
        if (policy.containsActionFor(state))
        {
            System.out.println("HIT!");
            return  policy.getAction(state);
        }
        else {
            System.out.println("MISS");
            return null;
        }
    }

}
