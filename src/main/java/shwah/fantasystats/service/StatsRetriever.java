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

@Service
public class StatsRetriever {
	//http://games.espn.com/ffl/api/v2/scoreboard?leagueId=1102919&seasonId=2018&matchupPeriodId=1
	
	String urlTest = "http://games.espn.com/ffl/api/v2/scoreboard?leagueId=1102919&seasonId=2018&matchupPeriodId=1";
	String baseUri = "http://games.espn.com/ffl/api/v2/scoreboard";
	
	Map<Integer, Integer> winsMap = new HashMap<>();

	
	public void pullStats() {
		
		int seasonNum = 2018;
		int matchupSize = 9;
		
		List<Integer> winsByTeamId = new ArrayList<>();
		
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
				JsonNode id1 = teamsNode.get(0).path("teamId");
				JsonNode score1 = teamsNode.get(0).path("score");
				JsonNode id2 = teamsNode.get(1).path("teamId");
				JsonNode score2 = teamsNode.get(1).path("score");
				
				scoreMap.put(score1.asDouble(), id1.asInt());
				scoreMap.put(score2.asDouble(), id2.asInt());
				
				if(score1.asDouble() > score2.asDouble()) {
					Integer wins = winsMap.get(id1.asInt());
					if(wins == null) {
						wins = Integer.valueOf(0);
					}
					wins++;
					winsMap.put(id1.asInt(), wins);
				} else {
					Integer wins = winsMap.get(id2.asInt());
					if(wins == null) {
						wins = Integer.valueOf(0);
					}
					wins++;
					winsMap.put(id2.asInt(), wins);
				}
				

				if(topHalf.size() < 5) {
					topHalf.add(score1.asDouble());
				}
				else if(topHalf.peek() < score1.asDouble()) {
					topHalf.poll();
					topHalf.add(score1.asDouble());
				}
				if(topHalf.size() < 5) {
					topHalf.add(score2.asDouble());
				}
				else if(topHalf.peek() < score2.asDouble()) {
					topHalf.poll();
					topHalf.add(score2.asDouble());
				}
				
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
