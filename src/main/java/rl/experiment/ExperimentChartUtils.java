package rl.experiment;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;


interface DataExtractor{
    XYSeries extract(String key, CollectedData data);
}

public class ExperimentChartUtils {

    private static final String T_EPISODE = "Episode";
    private static final String T_AVGREWARD = "Average Reward";
    private static final String T_CUMSTEP = "Total Step Count";

    static XYSeries seriesFromList(String key, List<Double> list)
    {
        XYSeries series = new XYSeries(key);

        for (int i = 0; i < list.size(); i++) {
            series.add(i+1,list.get(i));
        }
        return series;
    }


    public static JFreeChart createChart(String title, String xTitle, String yTitle, Map<String,List<Trial>> dataMap, DataExtractor extractor)
    {
        XYSeriesCollection dataset =new XYSeriesCollection();
        for (String key:dataMap.keySet()) {
            CollectedData collectedData = TrialsAverage.trialsAverage(dataMap.get(key));
            XYSeries series = extractor.extract(key,collectedData);
            dataset.addSeries(series);
        }

        return ChartFactory.createXYLineChart(title,xTitle,yTitle,dataset);
    }

    public static JFreeChart averageEpisodeRewardChart(String title, Map<String,List<Trial>> dataMap)
    {
        return createChart(title,T_EPISODE,T_AVGREWARD,dataMap,ExperimentChartUtils::averageEpisodeRewardsSeries);
    }

    public static JFreeChart cumulativeStepEpisodeChart(String title, Map<String,List<Trial>> dataMap)
    {
        return createChart(title,T_EPISODE,T_CUMSTEP,dataMap,ExperimentChartUtils::cumulativeStepEpisodeSeries);
    }


    public static XYSeries averageEpisodeRewardsSeries(String key, CollectedData collectedData)
    {
        return seriesFromList(key,collectedData.averageEpisodeReward());
    }

    public static XYSeries cumulativeStepEpisodeSeries(String key, CollectedData collectedData)
    {
        return seriesFromList(key,collectedData.cumulativeStepEpisode());
    }
    
    public static JFreeChart lineChartFromSeries(String title, String xTitle, String yTitle,List<XYSeries> series)
    {
        XYSeriesCollection dataset =new XYSeriesCollection();
        for (XYSeries s:series) {
            dataset.addSeries(s);
        }
        return ChartFactory.createXYLineChart(title,xTitle,yTitle,dataset);
    }

    public static void saveChart(JFreeChart chart, String chartFileName,int width, int height)
    {
        File imageFile = new File(chartFileName);

        try {
            ChartUtils.saveChartAsJPEG(imageFile,chart,width,height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
