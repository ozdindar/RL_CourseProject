package rl.gui;

import com.google.common.collect.HashBasedTable;
import rl.base.RLAction;
import rl.base.RLEnvironment;
import rl.base.RLState;
import rl.methods.dyna.DeterministicModel;
import rl.methods.dyna.DynaEvent;
import rl.tasks.grid.Grid2D;
import rl.tasks.grid.Grid2DState;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class DynaGridPanel extends JPanel implements DynaListener{

    private static final double NO_VALUE = 0.0;
    Grid2D grid;
    private final Color BackgroundColor= Color.WHITE;

    Grid2DState currentState;
    private HashBasedTable<RLState, RLAction, Double> qTable;
    private DeterministicModel model;
    private DynaEvent dynaEvent;
    private boolean visibleQValues = false;

    public DynaGridPanel(Grid2D grid) {
        super();
        this.grid = grid;
    }

    public void setVisibleQValues(boolean visibleQValues) {
        this.visibleQValues = visibleQValues;
    }

    private void drawGrid(Graphics g) {

        int cellSize = Math.min(getHeight(),getWidth())/Math.max(grid.getRowCount(),grid.getColCount());
        int tx = 0, lx=0;
        int ty = 0, ly=0;
        int rx=cellSize*grid.getColCount();
        int by = cellSize*grid.getRowCount();
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1f));
        for (int r = 0; r <= grid.getRowCount(); r++) {
            g.drawLine(tx,ty,tx,by);
            g.drawLine(lx,ly,rx,ly);
            tx +=cellSize;
            ly +=cellSize;
        }

        for (int c= 0; c <= grid.getColCount(); c++) {
            g.drawLine(tx,ty,tx,by);
            tx +=cellSize;
        }
    }


    void setWorld(Grid2D grid)
    {
        this.grid = grid;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        drawBackground(g);

        drawCells(g);
    }

    private void drawCells(Graphics g) {

        for (int r = 0; r < grid.getRowCount(); r++) {
            for (int c = 0; c < grid.getColCount(); c++) {
                    drawCell(g,r,c);
            }
        }
        drawGrid(g);
    }

    private void drawCell(Graphics g, int r, int c) {
        int worldLength = Math.min(getWidth(),getHeight());

        Grid2DState gstate = grid.getState(r,c);

        g.setColor(cellColor(gstate));

        int cellSize = worldLength/Math.max(grid.getRowCount(),grid.getColCount());

        Rectangle2D rect = new  Rectangle2D.Double(cellSize*c,cellSize*r,cellSize,cellSize);
        g.fillRect(cellSize*c,cellSize*r,cellSize,cellSize);

        if (currentState== gstate)
        {
            g.setColor(Color.red);
            g.fillOval((cellSize*c+ cellSize/4),cellSize*r+ cellSize/4,cellSize/2,cellSize/2);
        }

        if (visibleQValues)
            printQValues(g,gstate,rect);
    }

    private double getQValue(RLEnvironment env, RLState s, RLAction a) {

        if (qTable.contains(s,a))
            return qTable.get(s,a);
        else
        {
            return NO_VALUE;
        }
    }

    private void printQValues(Graphics g, Grid2DState gstate, Rectangle2D rect) {
        if (qTable==null)
            return;
        g.setColor(Color.BLACK);

        if (qTable.containsRow(gstate))
        {
            RLAction bestAction =grid.getActions(gstate).stream().max((a1,a2)->Double.compare(getQValue(grid,gstate,a1),getQValue(grid,gstate,a2))).get();
            double q = qTable.get(gstate,bestAction);
            if (q==NO_VALUE)
                return;



            g.drawString(String.format(bestAction + "-%.2f",q), (int)(rect.getCenterX()-rect.getWidth()/4),(int)rect.getCenterY());
        }
    }

    private Color cellColor(Grid2DState gstate) {


        if (gstate.isObstacle())
            return Color.BLACK;
        if (gstate.isTerminal())
            return Color.BLUE;
        if (grid.getInitialState()==gstate)
            return Color.ORANGE;
        if (qTable != null && qTable.containsRow(gstate))
            return new Color(0,255,0,100);
        else return Color.white;

    }

    private void drawBackground(Graphics g) {
        g.setColor(BackgroundColor);

        g.fillRect(0,0,getWidth(),getHeight());
    }




    public static void main(String[] args) {
        JFrame main= new JFrame("Game of Life");



        //main.add();

        main.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        main.setResizable(false);
        main.setSize(800,800);

        main.setVisible(true);


    }


    @Override
    public void dynaEvent(DynaEvent event, HashBasedTable<RLState, RLAction, Double> qTable, DeterministicModel model, RLState state) {
        this.qTable = qTable;
        this.model = model;
        this.dynaEvent = event;
        this.currentState = (Grid2DState) state;

        repaint();
    }
}
