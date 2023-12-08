package rl.experiment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Trial{
		
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
		 * The cumulative reward of the episode so far
		 */
		public double curEpisodeReward = 0.;
		
		/**
		 * The number of steps in the episode so far
		 */
		public int curEpisodeSteps = 0;
		
		/**
		 * the total number of steps in the trial
		 */
		public int totalSteps = 0;
		
		/**
		 * The total number of episodes in the trial
		 */
		public int totalEpisodes = 0;
		
		
		/**
		 * A list of the reward sequence in the current episode
		 */
		protected List<Double> curEpisodeRewards = new ArrayList<Double>();
		
		
		
		/**
		 * Updates all datastructures with the reward received from the last step
		 * @param r the last reward received
		 */
		public void stepIncrement(double r){
			
			accumulate(this.cumulativeStepReward, r);
			this.curEpisodeReward += r;
			this.curEpisodeSteps++;
			this.curEpisodeRewards.add(r);

		}
		
		
		/**
		 * Completes the last episode and sets up the datastructures for the next episode
		 */
		public void setupForNewEpisode(){
			accumulate(this.cumulativeEpisodeReward, this.curEpisodeReward);
			accumulate(this.cumulativeStepEpisode, this.curEpisodeSteps);
			
			double avgER = this.curEpisodeReward / (double)this.curEpisodeSteps;
			this.averageEpisodeReward.add(avgER);
			this.stepEpisode.add((double)this.curEpisodeSteps);
			
			Collections.sort(this.curEpisodeRewards);
			double med = 0.;
			if(this.curEpisodeSteps > 0){
				int n2 = this.curEpisodeSteps / 2;
				if(this.curEpisodeSteps % 2 == 0){
					double m = this.curEpisodeRewards.get(n2);
					double m2 = this.curEpisodeRewards.get(n2-1);
					med = (m + m2) / 2.;
				}
				else{
					med = this.curEpisodeRewards.get(n2);
				}
			}
			
			this.medianEpisodeReward.add(med);
			
			
			this.totalSteps += this.curEpisodeSteps;
			this.totalEpisodes++;
			
			this.curEpisodeReward = 0.;
			this.curEpisodeSteps = 0;
			
			this.curEpisodeRewards.clear();
			
		}

	static void accumulate(List<Double> list, double v){
		if(!list.isEmpty()){
			v += list.get(list.size()-1);
		}
		list.add(v);
	}
		
	}