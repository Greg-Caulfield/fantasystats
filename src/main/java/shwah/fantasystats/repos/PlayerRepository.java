package shwah.fantasystats.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import shwah.fantasystats.domain.Player;

@RepositoryRestResource
public interface PlayerRepository extends JpaRepository<Player,Integer> {

	Player findByTeamId(@Param("teamId") Integer teamId);
}
