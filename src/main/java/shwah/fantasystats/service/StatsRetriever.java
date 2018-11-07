package shwah.fantasystats.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import shwah.fantasystats.domain.Player;
import shwah.fantasystats.domain.Score;

@Service
public class StatsRetriever {
	//http://games.espn.com/ffl/api/v2/scoreboard?leagueId=1102919&seasonId=2018&matchupPeriodId=1
	
	String urlTest = "http://games.espn.com/ffl/api/v2/scoreboard?leagueId=1102919&seasonId=2018&matchupPeriodId=1";
	String baseUri = "http://games.espn.com/ffl/api/v2/scoreboard";
	
	Map<Integer, Integer> winsMap = new HashMap<>();

	
	public void pullStats() {
		
		int seasonNum = 2018;
		int matchupSize = 9;
		
		Map<Integer,Player> idToPlayerMap = new HashMap<>();
				
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
				
				Player p1 = idToPlayerMap.get(teamId1);
				if(p1 == null) {
					p1 = new Player();
				}
				
				Player p2 = idToPlayerMap.get(teamId2);
				if(p2 == null) {
					p2 = new Player();
				}
				
				Score scoreObj1 = new Score();
				Score scoreObj2 = new Score();
				
				scoreMap.put(score1, teamId1);
				scoreMap.put(score2, teamId2);
				
				if(score1 > score2) {
					Integer wins = winsMap.get(teamId1);
					if(wins == null) {
						wins = Integer.valueOf(0);
					}
					wins++;
					winsMap.put(teamId1, wins);
				} else {
					Integer wins = winsMap.get(teamId2);
					if(wins == null) {
						wins = Integer.valueOf(0);
					}
					wins++;
					winsMap.put(teamId2, wins);
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
				idToPlayerMap.put(teamId1, p1);
				idToPlayerMap.put(teamId2, p2);

			}
			
			for(Double doub : topHalf) {
				Integer teamId = scoreMap.get(doub);
				Integer wins = winsMap.get(teamId);
				if(wins == null) {
					wins = Integer.valueOf(0);
				}
				wins++;
				winsMap.put(teamId, wins);
			}
		}
		
		for(Integer team : winsMap.keySet()) {
			System.out.println("Team " + team +"," + winsMap.get(team) + "," + (matchupSize*2 -winsMap.get(team)));

		}

		//System.out.println(root);
		//JsonNode name = root.path("name");
	}

}
