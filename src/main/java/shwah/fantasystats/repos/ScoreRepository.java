package shwah.fantasystats.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import shwah.fantasystats.domain.Score;

@RepositoryRestResource
public interface ScoreRepository extends JpaRepository<Score,Integer> {

}
