package shwah.fantasystats.domain;

import java.io.Serializable;

public class Score implements Serializable {
	
	private double score;
	private int week;
	
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public int getWeek() {
		return week;
	}
	public void setWeek(int week) {
		this.week = week;
	}

}
