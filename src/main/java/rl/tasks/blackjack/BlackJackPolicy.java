package rl.tasks.blackjack;

import rl.base.DeterministicPolicy;
import rl.base.RLAction;
import rl.base.RLState;

import java.util.Set;

public class BlackJackPolicy implements DeterministicPolicy {

    @Override
    public void setAction(RLState state, RLAction action) {
        throw new RuntimeException("Not supported for this policy");
    }

    @Override
    public boolean containsActionFor(RLState state) {
        return true;
    }

    @Override
    public Set<RLState> getStates() {
        throw new RuntimeException("Not supported for this policy");
    }

    @Override
    public RLAction getAction(RLState s) {
        BlackJackState bs = (BlackJackState) s;

        if (bs.sum<20)
            return BLackJackAction.Hit;
        else return BLackJackAction.Stick;
    }


}
