package shwah.fantasystats.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import shwah.fantasystats.domain.Score;

@RepositoryRestResource
public interface ScoreRepository extends JpaRepository<Score,Integer> {

	//Find top 5 scores for a given week and year
	List<Score> findTop5ByWeekAndYearOrderByScore(@Param("week") int week,@Param("year") int year);
	
}
