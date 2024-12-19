package fact.it.teamservice.repository;

import fact.it.teamservice.model.Team;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Transactional
public interface TeamRepository extends JpaRepository<Team, Long> {
}
