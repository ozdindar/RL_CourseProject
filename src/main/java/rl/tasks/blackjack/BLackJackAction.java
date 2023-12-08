package rl.tasks.blackjack;

import rl.base.RLAction;

public enum BLackJackAction implements RLAction {
    Hit("H"), Stick("S");

    BLackJackAction(String shortName) {
        this.shortName = shortName;
    }

    final String shortName;

    String shortName()
    {
        return shortName;
    }


}
