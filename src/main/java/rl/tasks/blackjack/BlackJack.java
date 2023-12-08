package rl.tasks.blackjack;

import org.apache.commons.math3.util.Pair;
import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLState;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BlackJack implements RLEnvironment {

    private static final double LOSS_REWARD = -1;
    private static final double WIN_REWARD = +1;
    private static final double DRAW_REWARD = 0;
    Random rng = new SecureRandom();

    RLState currentState;
    boolean isOver;

    public List<RLState> getStates() {
        List<RLState> states = new ArrayList<>();

        for (int s = 11; s <22 ; s++) {
            for (int d = 1; d <10 ; d++) {
                states.add(new BlackJackState(s,d,true));
                states.add(new BlackJackState(s,d,false));
            }

        }
        return states ;
    }


    public RLState randomState() {
        int sum = 12 + rng.nextInt(10);
        int dealer = 1 + rng.nextInt(10);
        boolean usableAce = rng.nextBoolean();
        return new BlackJackState(sum,dealer,usableAce);
    }


    @Override
    public RLState reset() {
        currentState = randomState();
        isOver = false;
        return currentState;
    }

    @Override
    public Pair<RLState, Double> step(RLAction action) {
        BlackJackState bs = (BlackJackState) currentState;
        BLackJackAction ba = (BLackJackAction) action;
        double reward=0;
        if (ba == BLackJackAction.Hit)
            reward= playHit(bs);
        else reward = playStick(bs);
        return Pair.create(currentState,reward);
    }

    @Override
    public List<? extends RLAction> getActions(RLState state) {
        return Arrays.asList(BLackJackAction.Hit,BLackJackAction.Stick);
    }

    @Override
    public boolean isTerminal(RLState state) {
        return false;
    }

    @Override
    public boolean isOver() {
        return isOver;
    }

    @Override
    public RLState currentState() {
        return currentState;
    }


   private double playStick(BlackJackState bs) {
        isOver = true;
        int dealer = bs.dealerCard;
        boolean hasAce = dealer== 1;

        while(dealer<17)
        {
            int newCard = rng.nextInt(13);
            if (newCard==0 && !hasAce)
                hasAce=true;


            dealer += (newCard<10) ? (newCard+1):10;

            if (hasAce && dealer>6 && dealer<12)
                dealer += 10;
        }
        currentState = new BlackJackState(bs.sum,dealer, bs.usableAce);

        if (dealer>21 ||  bs.sum>dealer)
        {
             return WIN_REWARD;
        }
        return  bs.sum== dealer ? DRAW_REWARD:LOSS_REWARD ;
    }

    private double playHit(BlackJackState bs) {
        if (bs.sum>=21) {
            isOver = true;
            return LOSS_REWARD;
        }
        int sum = bs.sum;

        int newCard = rng.nextInt(13);
        sum += (newCard<10) ? (newCard+1):10;

        if (newCard==0 && !bs.usableAce) {
            bs.usableAce = true;
            sum +=10;
        }

        if (sum>21 && bs.usableAce)
        {
            sum -=10;
            bs.usableAce=false;
        }

        currentState = new BlackJackState(sum,bs.dealerCard,bs.usableAce);
        if (sum>21)
        {
            isOver = true;
            return LOSS_REWARD;
        }
        return DRAW_REWARD;
    }


}
