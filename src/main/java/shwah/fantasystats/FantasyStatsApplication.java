package shwah.fantasystats;

import java.util.Arrays;

import shwah.fantasystats.service.StatsRetriever;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FantasyStatsApplication 
{
    public static void main( String[] args )
    {
        SpringApplication.run(FantasyStatsApplication.class, args);

    }
    
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    	return args -> {
    		System.out.println("Running stat Retrieval...");
        	StatsRetriever statsRetriever = new StatsRetriever();
        	statsRetriever.pullStats();
    		
    	};
    }
}
