package rl.methods.dp;

import rl.MapBasedPolicy;
import rl.base.*;
import rl.tasks.grid.Grid2DModel;
import rl.tasks.grid.Grid2DPolicyRenderer;
import rl.tasks.grid.Grid2DValueRenderer;
import rl.tasks.recycler.BotPolicyRenderer;
import rl.tasks.recycler.BotValueRenderer;
import rl.tasks.recycler.RecycleRobot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ValueIteration implements DynamicProgramming{

    final double GAMMA = 0.99;
    final double EPSILON = 1e-7;
    final double INITIAL_V = 0.0;

    HashMap<RLState,Double> v;
    HashMap<RLState,Double> vNext;

    DeterministicPolicy policy;

    public void setValueRenderer(RLValueRenderer valueRenderer) {
        this.valueRenderer = valueRenderer;
    }

    public void setPolicyRenderer(RLPolicyRenderer policyRenderer) {
        this.policyRenderer = policyRenderer;
    }

    private RLValueRenderer valueRenderer;
    private RLPolicyRenderer policyRenderer;

    public void perform(EnvironmentModel model, int iteration)
    {
        init(model);
        double delta =0;

        for (int n = 0; n < iteration; n++) {
            v.putAll(vNext);
            delta = 0;

            for (RLState state: model.getStates())
            {
                update(model,state);
                double err = Math.abs(vNext.get(state)-v.get(state));
                if (err>delta)
                    delta=err;
            }
            if (delta<EPSILON)
                break;
        }
        printResults(model);
    }

    private void printResults(EnvironmentModel model) {
        if (valueRenderer != null)
            valueRenderer.renderValues(model,v);

        if (policyRenderer != null)
            policyRenderer.renderPolicy(model,policy);
    }

    List<Double> actionValues(EnvironmentModel environment, RLState state)
    {
        List<Double> actionValues=new ArrayList<>();
        for (RLAction action:environment.getActions(state))
        {
            double val =0;
            for(RLTransition transition: environment.getTransitions(state,action))
            {
                val += transition.prob()*transition.reward()+ transition.prob()*v.get(transition.to());
            }
             actionValues.add(val);
        }
        return actionValues;
    }
    private void update(EnvironmentModel environment, RLState state) {

        double newValue = 0;

        if (!environment.isTerminal(state)) {
            List<Double> actionValues = actionValues(environment,state);
            int best = maxIndex(actionValues);

            newValue = GAMMA*actionValues.get(best);

            policy.setAction(state,environment.getActions(state).get(best));
        }
        vNext.put(state,newValue);
    }

    private int maxIndex(List<Double> list) {
        int maxIndex= 0;

        for (int i = 1; i <list.size() ; i++) {
            if (list.get(i)>list.get(maxIndex))
            {
                maxIndex=i;
            }
        }
        return maxIndex;
    }


    private void init(EnvironmentModel model) {
        vNext = new HashMap<>();
        v = new HashMap<>();
        policy = new MapBasedPolicy();
        for (RLState state:model.getStates())
            vNext.put(state,INITIAL_V);
    }



    public static void main(String[] args) {
        botDemo();
    }

    private static void botDemo() {
        RecycleRobot robot = new RecycleRobot();

        ValueIteration vi = new ValueIteration();
        vi.setPolicyRenderer(new BotPolicyRenderer());
        vi.setValueRenderer(new BotValueRenderer());

        vi.perform(robot,100000);
    }

    private static void gridDemo() {
        Grid2DModel grid2D = new Grid2DModel(3,4);
        grid2D.setTerminal(0,3,5);
        grid2D.setTerminal(1,3,-10);
        grid2D.setObstacle(1,1);

        ValueIteration vi = new ValueIteration();
        vi.setPolicyRenderer(new Grid2DPolicyRenderer());
        vi.setValueRenderer(new Grid2DValueRenderer());

        vi.perform(grid2D,10000);
    }


}
