package fact.it.matchservice.repository;

import fact.it.matchservice.model.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {
}
