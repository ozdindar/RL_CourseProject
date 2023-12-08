package rl.tasks.recycler;

import rl.SimpleTransition;
import rl.base.EnvironmentModel;
import rl.base.RLAction;
import rl.base.RLState;
import rl.base.RLTransition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecycleRobot implements EnvironmentModel {

    private final double Beta = 0.5 ;
    private double Alpha = 0.3;

    static final double R_Wait= 0.5;
    static final double R_Search= 2.1;
    static final double R_Rec= 0.0;
    static double R_BatteryDown=-3.0;


    @Override
    public List<RLState> getStates() {
        return List.of(BatteryLevel.Low,BatteryLevel.High);
    }

    @Override
    public List<? extends RLTransition> getTransitions(RLState state, RLAction action) {
        switch ((BotAction)action)
        {
            case Wait : return waitTransitions(state);
            case Search:  return searchTransitions(state);
            case Recharge:  return rechargeTransitions(state);
        }
        return new ArrayList<>();
    }

    private List<? extends RLTransition> rechargeTransitions(RLState state) {
        return Arrays.asList(new SimpleTransition(state,BatteryLevel.High,1,RecycleRobot.R_Rec));
    }

    private List<? extends RLTransition> searchTransitions(RLState state) {
        BatteryLevel bs = (BatteryLevel) state;
        switch (bs)
        {
            case High : return Arrays.asList(new SimpleTransition(state,state,Alpha,RecycleRobot.R_Search),
                                            new SimpleTransition(state,BatteryLevel.Low,1-Alpha,RecycleRobot.R_Search) );
            case Low: return Arrays.asList(new SimpleTransition(state,state,Beta,RecycleRobot.R_Search),
                                            new SimpleTransition(state,BatteryLevel.High,1-Beta,RecycleRobot.R_BatteryDown));
        }
        return new ArrayList<>();
    }

    private List<? extends RLTransition> waitTransitions(RLState state) {
        return Arrays.asList(new SimpleTransition(state,state,1.0,RecycleRobot.R_Wait));
    }

    @Override
    public List<? extends RLAction> getActions(RLState state) {
        BatteryLevel battery = (BatteryLevel) state;
        switch (battery)
        {
            case High -> {
                return List.of(BotAction.Wait,BotAction.Search);
            }
            case Low -> {
                return List.of(BotAction.Wait, BotAction.Search,BotAction.Recharge);
            }
        }
        return List.of();
    }

    @Override
    public boolean isTerminal(RLState state) {
        return false;
    }




}
