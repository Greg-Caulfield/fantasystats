package shwah.fantasystats.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player implements Serializable{
	
	private int id;
	private String name;
	private List<Score> scores;
	private int wins;
	private int losses;
	private double pointsFor;
	private double pointsAgainst;
	
	public Player(int id) {
		this.id = id;
		this.name = "";
		this.scores = new ArrayList<>();
		this.wins = 0;
		this.losses = 0;
		this.pointsFor = 0;
		this.pointsAgainst = 0;
	}
	
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
	
	@Override
	public String toString() {
		return "Team " + id + "," + wins + "," + losses + "," + pointsFor;
	}


}
