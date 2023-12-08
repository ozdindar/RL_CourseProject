package rl.multiarmbandit;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class BanditExperiment {

    private static final String DEFAULT_CHART_FILENAME = "Experiment_Result_Chart.jpeg";

    List<BanditPlayer> players;
    Map<BanditPlayer,double[]> rewards;
    Bandit bandit;

    int runCount;

    String chartFileName;

    public BanditExperiment(Bandit bandit,int runCount) {
        this(bandit,runCount,DEFAULT_CHART_FILENAME);
    }

    public BanditExperiment(Bandit bandit,int runCount,String chartFileName) {
        this.bandit = bandit;
        players = new ArrayList<>();
        this.runCount = runCount;
        this.chartFileName = chartFileName;
    }

    public void addPlayer(BanditPlayer player)
    {
        players.add(player);
    }

    public void performExperiment(int rollcount) throws IOException {
        init(rollcount);
        for (int r = 1; r <= runCount; r++) {
            performOneRun(r,rollcount);
        }

        bandit.print();
        for (BanditPlayer player:players)
            player.printStats();

        saveChart();
    }


    private void saveChart() throws IOException {

        JFreeChart lineChartObject  = ChartFactory.createXYLineChart(
                "Rewards versus Time step",  // title
                "Step",             // x-axis label
                "Reward",   // y-axis label
                createDataSet());

        int width = 640;    /* Width of the image */
        int height = 480;   /* Height of the image */

        File lineChart = new File( chartFileName );

        ChartUtils.saveChartAsJPEG(lineChart ,lineChartObject, width ,height);
     }

    private XYDataset createDataSet() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        for (BanditPlayer player:rewards.keySet())
        {
            double[] prewards = rewards.get(player);
            String pName = player.getName();
            XYSeries series = new XYSeries(pName);
            for (int pr = 0; pr < prewards.length; pr++) {
                series.add((pr+1),prewards[pr]);
            }
            dataset.addSeries(series);
        }
        return dataset;
    }

    private void performOneRun(int run ,int rollcount ) {
        bandit.reset();
        initPlayers();
        for (int i = 0; i < rollcount; i++) {
            for (BanditPlayer player:players)
            {
                int a = player.getAction();
                double reward = bandit.roll(a);
                double[] playerRewards = rewards.get(player);
                playerRewards[i] += (1.0/run)*(reward-playerRewards[i]);
                player.getReward(a,reward);
            }
        }
    }

    private void initPlayers() {
        for (BanditPlayer player:players)
            player.init(bandit.armCount());
    }

    private void init(int rollCount) {

        rewards = new HashMap<>();
        for (BanditPlayer player:players)
        {
            rewards.put(player, new double[rollCount]);
        }
    }

}
