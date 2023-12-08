package rl.methods.functionapprox.linear;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import rl.base.RLEnvironment;
import rl.tasks.randomwalk.RandomWalkLong;

import java.io.File;
import java.io.IOException;

public class RWAFRenderer  implements ApproximationFunctionRenderer{

    String plotFileName;

    public RWAFRenderer(String plotFileName) {
        this.plotFileName = plotFileName;
    }



    @Override
    public void render(RLEnvironment env, ApproximationFunction vFunction, FeatureExtractor featureExtractor, double[] w) {
        RandomWalkLong rwl = (RandomWalkLong) env;
        int pathLength = rwl.pathLength();

        double[] stateValues = new  double[pathLength];
        for (int i = 0; i < pathLength; i++) {
            double[] features= featureExtractor.featuresOf(rwl.getState(i));
            stateValues[i] = vFunction.value(features);
            System.out.println(String.format("[%d] : %.2f",i,stateValues[i]));

        }


        if (!plotFileName.isEmpty())
        {
            try {
                saveChart(pathLength,stateValues);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveChart(int pathLength, double[] stateValues) throws IOException {

        JFreeChart lineChartObject  = ChartFactory.createXYLineChart(
                "Approximated Values of States",  // title
                "State",             // x-axis label
                "Value",   // y-axis label
                createDataSet(pathLength,stateValues));

        int width = 640;    /* Width of the image */
        int height = 480;   /* Height of the image */
        File lineChart = new File( plotFileName );

        ChartUtils.saveChartAsJPEG(lineChart ,lineChartObject, width ,height);
    }

    private XYDataset createDataSet(int pathLength, double[] stateValues) {
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries series = new XYSeries("");

        for (int i = 0; i < pathLength; i++) {
            series.add(i,stateValues[i]);
        }
        dataset.addSeries(series);
        return dataset;
    }
}
