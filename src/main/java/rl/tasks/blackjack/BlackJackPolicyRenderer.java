package rl.tasks.blackjack;

import rl.base.DeterministicPolicy;
import rl.base.EnvironmentModel;
import rl.base.RLPolicy;
import rl.base.RLPolicyRenderer;

public class BlackJackPolicyRenderer implements RLPolicyRenderer {
    @Override
    public void renderPolicy(EnvironmentModel environment, RLPolicy pi) {
        renderPolicy(pi);
    }

    @Override
    public void renderPolicy(RLPolicy pi) {

        DeterministicPolicy dp = (DeterministicPolicy) pi;
        for (int s = 12; s < 22; s++) {
            for (int d = 1; d <=10; d++) {
                BlackJackState bs = new BlackJackState(s,d,false);
                System.out.print( dp.getAction(bs)+ "  ");
            }
            System.out.println();
        }
        for (int s = 12; s < 22; s++) {
            for (int d = 1; d <=10; d++) {
                BlackJackState bs = new BlackJackState(s,d,true);
                System.out.print( dp.getAction(bs)+ "  ");
            }
            System.out.println();
        }
    }
}
