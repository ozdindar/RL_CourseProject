package rl.tasks.pathfinding;


import org.apache.commons.math3.util.Pair;
import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLState;
import rl.methods.temporaldifference.QLearning;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PathFinding implements RLEnvironment {

    Graph<Integer> graph;
    List<RLState> states;
    RLState currentState;

    List<Integer> terminals= new ArrayList<>();
    private final double TERMINAL_REWARD=1.0;


    public PathFinding(Graph<Integer> graph) {
        this.graph = graph;
        buildStates();
    }

    private void buildStates() {
        states = graph.getVertices().stream().map(GraphState::new).collect(Collectors.toList());

        buildActions();
    }

    public void setTerminal(int id)
    {
        if (terminals.contains(id))
            return;

        terminals.add(id);

    }

    void buildActions() {
        for (RLState state:states)
        {
            GraphState gState = (GraphState) state;
            gState.clearActions();
            for (Integer neighbor : graph.successors(gState.id))
            {
                gState.addAction(new GraphAction(gState.id,neighbor));
            }
        }
    }


    public List<RLState> getStates() {
        return states;
    }

    @Override
    public RLState currentState() {
        return currentState;
    }

    @Override
    public RLState reset() {
        currentState = states.get(new SecureRandom().nextInt(states.size()));
        return currentState;
    }

    @Override
    public Pair<RLState, Double> step(RLAction action) {
        GraphAction ga = (GraphAction) action;
        currentState = getState(ga.to);
        double r = isTerminal(currentState)? TERMINAL_REWARD:0;
        return Pair.create(currentState,r);
    }

    @Override
    public List<? extends RLAction> getActions(RLState state) {
        GraphState gs = (GraphState) state;
        return gs.getActions();
    }

    @Override
    public boolean isTerminal(RLState state) {
        GraphState gs = (GraphState) state;
        return terminals.contains(gs.id);
    }



    public GraphState getState(int id) {
        return (GraphState) states.stream().filter((x)->((GraphState)x).id==id).findFirst().get();
    }

    public static void main(String[] args) {

        Graph<Integer> graph = new Graph<>();
        graph.addVertices(Arrays.asList(0,1,2,3,4,5));
        graph.successors(0).add(4);
        graph.successors(1).addAll(Arrays.asList(3,5));
        graph.successors(2).addAll(Arrays.asList(3));
        graph.successors(3).addAll(Arrays.asList(1,2,4));
        graph.successors(4).addAll(Arrays.asList(0,3,5));

        PathFinding pathFinding = new PathFinding(graph);
        pathFinding.setTerminal(5);
        QLearning qLearning = new QLearning(0.1,new GraphPolicyRenderer());

        qLearning.run(pathFinding, 1000);
    }
}
