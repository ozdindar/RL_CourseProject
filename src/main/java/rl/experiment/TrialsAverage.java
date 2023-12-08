package rl.experiment;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class TrialsAverage implements CollectedData{
		
		/**
		 * Stores the cumulative reward by step
		 */
		public List<Double> cumulativeStepReward = new ArrayList<Double>();
		
		/**
		 * Stores the cumulative reward by episode
		 */
		public List<Double> cumulativeEpisodeReward = new ArrayList<Double>();
		
		/**
		 * Stores the average reward by episode
		 */
		public List<Double> averageEpisodeReward = new ArrayList<Double>();
		
		/**
		 * Stores the median reward by episode
		 */
		public List<Double> medianEpisodeReward = new ArrayList<Double>();
		
		
		/**
		 * Stores the cumulative steps by episode
		 */
		public List<Double> cumulativeStepEpisode = new ArrayList<Double>();
		
		/**
		 * Stores the steps by episode
		 */
		public List<Double> stepEpisode = new ArrayList<Double>();
		
		
		/**
		 * the total number of steps in the trial
		 */
		public double totalSteps = 0;
		
		/**
		 * The total number of episodes in the trial
		 */
		public double totalEpisodes = 0;
		

		static List<Double> averageList(List<List<Double>> lists)
		{
			List<Double> avgList = new ArrayList<>();

			int maxSize = lists.stream().mapToInt(List::size).max().getAsInt();

			for (int i = 0; i < maxSize; i++) {
				double avg = 0;
				int count =0;
				for (List<Double> l:lists){
					if (l.size()>i)
					{
						avg += l.get(i);
						count++;
					}
				}
				avgList.add(avg/count);
			}

			return avgList;
		}

		static TrialsAverage trialsAverage(List<Trial> trials)
		{
			TrialsAverage trialsAverage = new TrialsAverage();

			List<List<Double>> lAverageEpisodeReward = trials.stream().map((t)->t.averageEpisodeReward).collect(Collectors.toList());
			trialsAverage.averageEpisodeReward = averageList(lAverageEpisodeReward);

			List<List<Double>> lCumulativeEpisodeReward = trials.stream().map((t)->t.cumulativeEpisodeReward).collect(Collectors.toList());
			trialsAverage.cumulativeEpisodeReward = averageList(lCumulativeEpisodeReward);

			List<List<Double>> lCumulativeStepEpisode= trials.stream().map((t)->t.cumulativeStepEpisode).collect(Collectors.toList());
			trialsAverage.cumulativeStepEpisode = averageList(lCumulativeStepEpisode);

			List<List<Double>> lCumulativeStepReward= trials.stream().map((t)->t.cumulativeStepReward).collect(Collectors.toList());
			trialsAverage.cumulativeStepReward = averageList(lCumulativeStepReward);

			List<List<Double>> lMedianEpisodeReward= trials.stream().map((t)->t.medianEpisodeReward).collect(Collectors.toList());
			trialsAverage.medianEpisodeReward = averageList(lMedianEpisodeReward);

			List<List<Double>> lStepEpisode= trials.stream().map((t)->t.stepEpisode).collect(Collectors.toList());
			trialsAverage.stepEpisode = averageList(lStepEpisode);

			trialsAverage.totalEpisodes = trials.stream().mapToInt((t)->t.totalEpisodes).average().getAsDouble();
			trialsAverage.totalSteps = trials.stream().mapToInt((t)->t.totalSteps).average().getAsDouble();

			return trialsAverage;

		}

	@Override
	public List<Double> averageEpisodeReward() {
		return averageEpisodeReward;
	}

	@Override
	public List<Double> cumulativeStepEpisode() {
		return cumulativeStepEpisode;
	}
}