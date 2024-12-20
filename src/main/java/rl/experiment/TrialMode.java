package rl.experiment;


/**
 * Enumerator for specifying the what kinds of plots for each {@link PerformanceMetric} will be plotted by {@link PerformancePlotter}.
 * The MOSTRECENTTTRIALONLY mode will result in only the most recent trial's performance being displayed. TRIALAVERAGESONLY will
 * result in only plots for the trial averages to be shown. MOSTRECENTANDAVERAGE will result in both the most recent trial and the trial
 * average plots to be shown.
 * @author James MacGlashan
 *
 */
public enum TrialMode {
	MOST_RECENT_TRIAL_ONLY,
	TRIAL_AVERAGES_ONLY,
	MOST_RECENT_AND_AVERAGE;
	
	/**
	 * Returns true if the most recent trial plots will be plotted by this mode.
	 * @return true if the most recent trial plots will be plotted by this mode; false otherwise.
	 */
	public boolean mostRecentTrialEnabled(){
		return this == MOST_RECENT_TRIAL_ONLY || this == MOST_RECENT_AND_AVERAGE;
	}
	
	
	/**
	 * Returns true if the trial average plots will be plotted by this mode.
	 * @return true if the trial average plots will be plotted by this mode; false otherwise.
	 */
	public boolean averagesEnabled(){
		return this == TRIAL_AVERAGES_ONLY || this == MOST_RECENT_AND_AVERAGE;
	}
}
