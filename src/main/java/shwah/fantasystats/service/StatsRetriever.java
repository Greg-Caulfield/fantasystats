package shwah.fantasystats.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import shwah.fantasystats.domain.Player;
import shwah.fantasystats.domain.Score;
import shwah.fantasystats.repos.PlayerRepository;
import shwah.fantasystats.repos.ScoreRepository;

@Service
public class StatsRetriever {
	//http://games.espn.com/ffl/api/v2/scoreboard?leagueId=1102919&seasonId=2018&matchupPeriodId=1
	
	String baseUri = "http://games.espn.com/ffl/api/v2/scoreboard";
		
	@Autowired
	PlayerRepository playerRepository;
	
	@Autowired
	ScoreRepository scoreRepository;

	@Transactional
	public void pullStats() {
		
		int seasonNum = 2018;
		int matchupSize = 9;
						
		for(int i = 1; i <= matchupSize; i++) {
			Queue<Double> topHalf = new PriorityQueue<>(5);
			Map<Double, Integer> scoreMap = new HashMap<>();


			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUri)
					.queryParam("leagueId",1102919)
					.queryParam("seasonId", seasonNum)
					.queryParam("matchupPeriodId",i);
			
			
			
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<String> response = restTemplate.getForEntity(builder.toUriString(), String.class);
			ObjectMapper mapper = new ObjectMapper();
			JsonNode root = null;
			try {
				root = mapper.readTree(response.getBody());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			JsonNode matchup = root.path("scoreboard").path("matchups");
			for(int j = 0; j < matchup.size(); j++){
				JsonNode teamsNode = matchup.get(j).path("teams");
				Integer teamId1 = teamsNode.get(0).path("teamId").asInt();
				Integer teamId2 = teamsNode.get(1).path("teamId").asInt();
				Double score1 = teamsNode.get(0).path("score").asDouble();
				Double score2 = teamsNode.get(1).path("score").asDouble();
				Double pointsFor1 = teamsNode.get(0).path("team").path("record").path("pointsFor").asDouble();
				Double pointsFor2 = teamsNode.get(1).path("team").path("record").path("pointsFor").asDouble();
				
				Double pointsAgainst1 = teamsNode.get(0).path("team").path("record").path("pointsAgainst").asDouble();
				Double pointsAgainst2 = teamsNode.get(1).path("team").path("record").path("pointsAgainst").asDouble();
				
				String teamNamea1= teamsNode.get(0).path("team").path("teamLocation").asText();
				String teamNameb1= teamsNode.get(0).path("team").path("teamNickname").asText();

				
				String teamNamea2= teamsNode.get(1).path("team").path("teamLocation").asText();
				String teamNameb2= teamsNode.get(1).path("team").path("teamNickname").asText();
				
				Player p1 = playerRepository.findByTeamId(teamId1);
				if(p1 == null) {
					p1 = new Player();
					p1.setTeamId(teamId1);
				}
				p1.setName(teamNamea1.trim() + " " + teamNameb1);
				
				Player p2 = playerRepository.findByTeamId(teamId2);
				if(p2 == null) {
					p2 = new Player();
					p2.setTeamId(teamId2);
				}
				p2.setName(teamNamea2.trim() + " " + teamNameb2);

				Score scoreObj1 = new Score();
				Score scoreObj2 = new Score();
				
				scoreObj1.setScore(score1);
				scoreObj1.setWeek(i);
				scoreObj1.setYear(seasonNum);
				scoreObj1.setPlayer(p1);
				
				scoreObj2.setScore(score2);
				scoreObj2.setWeek(i);
				scoreObj2.setYear(seasonNum);
				scoreObj2.setPlayer(p2);
				
				scoreObj1 = scoreRepository.save(scoreObj1);
				scoreObj2 = scoreRepository.save(scoreObj2);
				
				List<Score> scoreArr1 = p1.getScores();
				List<Score> scoreArr2 = p2.getScores();
				
				scoreArr1.add(scoreObj1);
				scoreArr2.add(scoreObj2);

				scoreMap.put(score1, teamId1);
				scoreMap.put(score2, teamId2);
				
				if(score1 > score2) {
					p1.setWins(p1.getWins()+1);

				} else {
					p2.setWins(p2.getWins()+1);
				}
				
				if(topHalf.size() < 5) {
					topHalf.add(score1);
				}
				else if(topHalf.peek() < score1) {
					topHalf.poll();
					topHalf.add(score1);
				}
				if(topHalf.size() < 5) {
					topHalf.add(score2);
				}
				else if(topHalf.peek() < score2) {
					topHalf.poll();
					topHalf.add(score2);
				}
				p1.setPointsFor(pointsFor1);
				p1.setPointsAgainst(pointsAgainst1);
				p2.setPointsFor(pointsFor2);
				p2.setPointsAgainst(pointsAgainst2);

				
				p1.setScores(scoreArr1);
				p2.setScores(scoreArr2);
				
				
				playerRepository.save(p1);
				playerRepository.save(p2);
				
				
			}
			
			//last matchup in the week, calculate extra wins			
			List<Score> scores = scoreRepository.findTop5ByWeekAndYearOrderByScoreDesc(i, 2018);
			for(Score score : scores) {
				Player p = score.getPlayer();
				p.setWins(p.getWins()+1);
				playerRepository.save(p);
			}	
		}
		
		for(int i = 1; i < 10; i++) {
			List<Score> scores = scoreRepository.findTop5ByWeekAndYearOrderByScoreDesc(i, 2018);
			for(Score s : scores) {
				System.out.print(s.getPlayer().getTeamId() + " " + s.getScore() + ",");
			}
			System.out.println();
		}
		
		for(Player p : playerRepository.findAll()) {
			p.setLosses(matchupSize*2 -p.getWins());
			playerRepository.save(p);
			System.out.println(p);
		}
	}

}
