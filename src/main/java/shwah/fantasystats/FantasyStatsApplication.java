package shwah.fantasystats;

import java.util.Arrays;

import shwah.fantasystats.service.StatsRetriever;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = { "shawh.fantasystats.controller", "shawh.fantasystats.domain", "shwah.fantasystats.service", "shawh.fantasystats.repos"})
@EnableJpaRepositories(basePackages = { "shwah.fantasystats.repos" })
@EntityScan(basePackages = { "shwah.fantasystats.domain" })
public class FantasyStatsApplication 
{
    public static void main( String[] args )
    {
        SpringApplication.run(FantasyStatsApplication.class, args);

    }
    
    @Autowired
    StatsRetriever statsRetriever;
    
    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
    	return args -> {
    	
    		System.out.println("Running stat Retrieval...");
        	statsRetriever.pullStats();
    		
    	};
    }
}
  