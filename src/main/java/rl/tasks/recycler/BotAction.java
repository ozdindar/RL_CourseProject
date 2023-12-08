package rl.tasks.recycler;

import rl.base.RLAction;

public enum BotAction implements RLAction {
    Wait("W"), Search("S"), Recharge("R");

    BotAction(String shortName) {
        this.shortName = shortName;
    }

    final String shortName;

    String shortName()
    {
        return shortName;
    }
}
