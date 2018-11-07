package shwah.fantasystats.domain;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable{
	
	private int id;
	private String name;
	private List<Score> scores;
	private int wins;
	private int losses;
	private double pointsFor;
	private double pointsAgainst;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Score> getScores() {
		return scores;
	}
	public void setScores(List<Score> scores) {
		this.scores = scores;
	}
	public int getWins() {
		return wins;
	}
	public void setWins(int wins) {
		this.wins = wins;
	}
	public int getLosses() {
		return losses;
	}
	public void setLosses(int losses) {
		this.losses = losses;
	}
	public double getPointsFor() {
		return pointsFor;
	}
	public void setPointsFor(double pointsFor) {
		this.pointsFor = pointsFor;
	}
	public double getPointsAgainst() {
		return pointsAgainst;
	}
	public void setPointsAgainst(double pointsAgainst) {
		this.pointsAgainst = pointsAgainst;
	}


}
