package rl;

import org.apache.commons.math3.util.Pair;
import rl.base.EnvironmentObserver;
import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EnvironmentServer implements RLEnvironment {
    RLEnvironment environment;
    List<EnvironmentObserver> observers;

    long stepCounter;
    long stepDelayStart;
    long stepDelay;

    public void setStepDelayStart(long stepDelayStart) {
        this.stepDelayStart = stepDelayStart;
    }

    public void setStepDelay(long stepDelay) {
        this.stepDelay = stepDelay;
    }

    public EnvironmentServer(RLEnvironment environment, EnvironmentObserver...observers) {
        this.environment = environment;
        this.observers = new ArrayList<>();
        this.observers.addAll(Arrays.asList(observers));
    }

    @Override
    public RLState currentState() {
        return environment.currentState();
    }

    @Override
    public RLState reset() {
        for( EnvironmentObserver observer:observers)
            observer.environmentReset(environment);
        return environment.reset();
    }

    @Override
    public Pair<RLState, Double> step(RLAction action) {
        stepCounter++;
        Pair<RLState,Double> result = environment.step(action);
        for (EnvironmentObserver observer:observers)
            observer.environmentStep(result);

        if (stepDelay>0 &&  stepCounter>stepDelayStart) {
            try {
                Thread.sleep(stepDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    @Override
    public List<? extends RLAction> getActions(RLState state) {
        return environment.getActions(state);
    }

    @Override
    public boolean isTerminal(RLState state) {
        return environment.isTerminal(state);
    }
}
