package shwah.fantasystats.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;


@Entity
public class Player extends AbstractDomain {


	@Column
	private int teamId;
	
	@Column
	private String name;
	
	@OneToMany(mappedBy="player")
	private List<Score> scores = new ArrayList<>();
	
	@Column
	private int wins;
	
	@Column
	private int losses;
	
	@Column
	private double pointsFor;
	
	@Column
	private double pointsAgainst;
	
	public int getTeamId() {
		return teamId;
	}
	public void setTeamId(int teamId) {
		this.teamId = teamId;
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
		return "Team " + teamId + "," + wins + "," + losses + "," + pointsFor;
	}


}
