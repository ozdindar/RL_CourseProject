package rl.tasks.rod;

import org.apache.commons.math3.util.Pair;
import rl.base.EnvironmentObserver;
import rl.base.RLEnvironment;
import rl.base.RLState;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class RodPanel extends JPanel implements EnvironmentObserver {

    private static final Color ROD_COLOR = Color.RED;
    private static final Color INITIAL_ROD_COLOR = Color.GREEN;
    private static final Color TERMINAL_ROD_COLOR = Color.ORANGE;
    private final Color BackgroundColor = Color.WHITE;
    private final Color ObstacleColor   = Color.BLUE;
    private final Color FrameColor = Color.BLACK;

    RodManeuvering env;


    public RodPanel(RodManeuvering env) {
        this.env = env;
    }

    @Override
    public void environmentReset(RLEnvironment environment) {
        repaint();
    }

    @Override
    public void environmentStep(Pair<RLState, Double> result) {
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);

        drawObstacles(g);

        drawRods(g);
    }

    private void drawRods(Graphics g) {
        Stroke stroke =  ((Graphics2D)g).getStroke();
        ((Graphics2D) g).setStroke(new BasicStroke(6));

        Line2D rodLine = env.rodLine();
        if (rodLine !=null) {
            g.setColor(ROD_COLOR);
            g.drawLine((int) rodLine.getX1(), (int) rodLine.getY1(), (int) rodLine.getX2(), (int) rodLine.getY2());
            g.fillOval((int)rodLine.getX1()-7,(int)rodLine.getY1()-7,14,14);
            g.fillOval((int)rodLine.getX2()-7,(int)rodLine.getY2()-7,14,14);
        }
        rodLine = env.rodLine((RodState) env.initialState);
        g.setColor(INITIAL_ROD_COLOR);
        g.drawLine((int) rodLine.getX1(), (int) rodLine.getY1(), (int) rodLine.getX2(), (int) rodLine.getY2());

        rodLine = env.rodLine((RodState) env.terminalState);
        g.setColor(TERMINAL_ROD_COLOR);
        g.drawLine((int) rodLine.getX1(), (int) rodLine.getY1(), (int) rodLine.getX2(), (int) rodLine.getY2());


        ((Graphics2D)g).setStroke(stroke);
    }

    private void drawObstacles(Graphics g) {
        if (env==null)
            return;
        g.setColor(ObstacleColor);
        for( Polygon p:env.obstacles)
        {
            g.fillPolygon(p);
        }
    }

    private void drawBackground(Graphics g) {
        g.setColor(BackgroundColor);
        g.fillRect(0,0,getWidth(),getHeight());

        g.setColor(FrameColor);
        g.drawRect(0,0,env.width,env.height);
    }
}
