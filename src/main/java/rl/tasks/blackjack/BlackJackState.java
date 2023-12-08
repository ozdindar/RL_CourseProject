package rl.tasks.blackjack;

import rl.base.RLState;

public class BlackJackState implements RLState {

    int sum;
    int dealerCard;
    boolean usableAce;

    public BlackJackState(int sum, int dealerCard, boolean usableAce) {
        this.sum = sum;
        this.dealerCard = dealerCard;
        this.usableAce = usableAce;

    }

     @Override
    public int hashCode() {
        return (sum+"-"+dealerCard+usableAce).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof  BlackJackState))
            return false;
        return (sum == ((BlackJackState) obj).sum&& dealerCard== ((BlackJackState) obj).dealerCard&& usableAce== ((BlackJackState) obj).usableAce);
    }

    @Override
    public String toString() {
        return sum + "-" + dealerCard + "-"+ usableAce;
    }
}
