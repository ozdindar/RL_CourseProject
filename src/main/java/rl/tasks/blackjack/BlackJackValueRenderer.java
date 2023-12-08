package rl.tasks.blackjack;

import rl.base.RLEnvironment;
import rl.base.RLState;
import rl.base.RLValueRenderer;

import java.util.HashMap;
import java.util.Map;

public class BlackJackValueRenderer implements RLValueRenderer {
    @Override
    public void renderValues(RLEnvironment environment, HashMap<RLState, Double> v) {
        renderValues(v);
    }

    @Override
    public void renderValues(Map<RLState, Double> v) {
        System.out.println("NO USABLE ACE");
        for (int s = 12; s < 22; s++) {
            System.out.print("Hand:"+s+"     ");
            for (int d = 1; d <10; d++) {
                BlackJackState bs = new BlackJackState(s,d,false);
                System.out.print( v.get(bs)+ "  ");
            }
            System.out.println();
        }
        System.out.println("USABLE ACE");
        for (int s = 12; s < 22; s++) {
            System.out.print("Hand:"+s+"     ");
            for (int d = 1; d <10; d++) {
                BlackJackState bs = new BlackJackState(s,d,true);
                System.out.print( v.get(bs)+ "  ");
            }
            System.out.println();
        }
    }
}
