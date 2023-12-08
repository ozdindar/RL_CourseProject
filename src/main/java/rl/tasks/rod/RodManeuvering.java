package rl.tasks.rod;

import org.apache.commons.math3.util.Pair;
import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLState;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class RodManeuvering implements RLEnvironment {

    static final int ANGLE_COUNT = 18;
    static final double UNIT_ANGLE = 2*Math.PI/ANGLE_COUNT;
    static final int UNIT_LENGTH = 10;
    static final int ROD_LENGTH = 60;
    private static final double NO_REWARD = 0;
    private static final double TERMINAL_REWARD = 10;

    List<Polygon>obstacles;

    int width;
    int height;

    RLState initialState;
    RLState currentState;
    RLState terminalState;

    public RodManeuvering(int width, int height, RLState initialState, RLState terminalState) {
        this.width = width;
        this.height = height;
        this.initialState = initialState;
        this.terminalState = terminalState;
        obstacles = new ArrayList<>();
    }

    @Override
    public RLState currentState() {
        return currentState;
    }

    @Override
    public RLState reset() {
        currentState =initialState;
        return currentState;
    }

    @Override
    public Pair<RLState, Double> step(RLAction action) {

        double reward = NO_REWARD;

        RLState nextState = virtualNext(currentState,action);

        if (!colliding(nextState))
            currentState = nextState;

        if (isTerminal(currentState))
            reward = TERMINAL_REWARD;
        return Pair.create(currentState,reward);
    }

    private boolean colliding(RLState state) {
        RodState rodState = (RodState) state;
        Line2D.Double rodLine = rodLine(rodState);

        if (    isOutside( rodLine.getX1(),rodLine.getY1()) ||
                isOutside( rodLine.getX2(),rodLine.getY2())
            )
            return true;


        for (Polygon p:obstacles)
        {
            try {
                if (!GeometryUtils.getIntersections(p,rodLine).isEmpty())
                    return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            if (p.contains(rodLine.getP1())|| p.contains(rodLine.getP2()))
                return true;
        }

        return false;
    }

    private boolean isOutside(double x1, double y1) {
        return x1>width || x1<0 || y1<0 || y1>height;
    }

    Line2D rodLine()
    {
        if (currentState!=null)
            return rodLine((RodState) currentState);
        else return null;
    }
    Line2D.Double rodLine(RodState rodState) {
        double cx = rodState.x* UNIT_LENGTH;
        double cy = rodState.y* UNIT_LENGTH;

        double x1 = cx + (ROD_LENGTH/2.0)*Math.cos(rodState.angle*UNIT_ANGLE);
        double y1 = cy + (ROD_LENGTH/2.0)*Math.sin(rodState.angle*UNIT_ANGLE);

        double x2 = cx - (ROD_LENGTH/2.0)*Math.cos(rodState.angle*UNIT_ANGLE);
        double y2 = cy - (ROD_LENGTH/2.0)*Math.sin(rodState.angle*UNIT_ANGLE);

        return new Line2D.Double(x1,y1,x2,y2);
    }

    private RLState virtualNext(RLState currentState, RLAction action) {
        RodState rs = (RodState) currentState;
        RodAction ra = (RodAction) action;
        switch (ra)
        {
            case Up: return new RodState(rs.x,rs.y-1,rs.angle);
            case Down: return new RodState(rs.x,rs.y+1,rs.angle);
            case Left: return new RodState(rs.x-1,rs.y,rs.angle);
            case Right: return new RodState(rs.x+1,rs.y,rs.angle);
            case CCW: return new RodState(rs.x,rs.y,(rs.angle+1)%ANGLE_COUNT);
            case CW: return new RodState(rs.x,rs.y,(rs.angle-1)%ANGLE_COUNT);
        }
        return rs;
    }

    @Override
    public List<? extends RLAction> getActions(RLState state) {
        return Arrays.asList(   RodAction.Up,
                                RodAction.Down,
                                RodAction.Left,
                                RodAction.Right,
                                RodAction.CW,
                                RodAction.CCW);
    }



    @Override
    public boolean isTerminal(RLState state) {
        RodState rs = (RodState) terminalState;
        RodState cs = (RodState) currentState;
        return  Math.abs(cs.x - rs.x)<2 && Math.abs(cs.y-rs.y)<2 && Math.abs(cs.angle-rs.angle)<15;
    }

    public void addObstacle(Polygon polygon) {
        obstacles.add(polygon);
    }
}
