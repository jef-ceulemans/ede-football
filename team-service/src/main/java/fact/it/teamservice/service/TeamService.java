package fact.it.teamservice.service;

import fact.it.teamservice.dto.TeamRequest;
import fact.it.teamservice.dto.TeamResponse;
import fact.it.teamservice.model.Team;
import fact.it.teamservice.repository.TeamRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    // Methode om teamdata te laden voor initiële testdata
    @PostConstruct
    public void loadData() {

        if (teamRepository.count() <= 0) {
            Team team1 = new Team();
            team1.setName("FC Barcelona");
            team1.setCountry("Spain");
            team1.setStadium("Camp Nou");

            Team team2 = new Team();
            team2.setName("Manchester United");
            team2.setCountry("England");
            team2.setStadium("Old Trafford");

            Team team3 = new Team();
            team3.setName("Lierse");
            team3.setCountry("Belgium");
            team3.setStadium("Herman van den poorten stadion");


            teamRepository.save(team1);
            teamRepository.save(team2);
            teamRepository.save(team3);

        }
    }

    // Methode om alle teams op te halen
    @Transactional(readOnly = true)
    public List<TeamResponse> getTeams() {
        return teamRepository.findAll().stream()
                .map(team -> TeamResponse.builder()
                        .id(team.getId())
                        .name(team.getName())
                        .country(team.getCountry())
                        .stadium(team.getStadium())
                        .build())
                .toList();
    }

    // Methode om een team op te halen op basis van teamId
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long teamId) {
        Team team = teamRepository.findById(teamId).orElse(null);
        if (team != null) {
            return TeamResponse.builder()
                    .id(team.getId())
                    .name(team.getName())
                    .country(team.getCountry())
                    .stadium(team.getStadium())
                    .build();
        }
        return null; // Als team niet gevonden wordt, kan null of een custom exception worden teruggegeven
    }

    // Methode om een nieuw team toe te voegen
    @Transactional
    public boolean registerTeam(TeamRequest teamRequest) {
        Team team = Team.builder()
                .name(teamRequest.getName())
                .country(teamRequest.getCountry())
                .stadium(teamRequest.getStadium())
                .build();

        teamRepository.save(team);
        return true; // Retourneer true als het team succesvol is toegevoegd
    }

    // Methode om een team te verwijderen
    @Transactional
    public boolean deleteTeam(Long id) {
        if (teamRepository.existsById(id)) {
            teamRepository.deleteById(id);
            return true; // Team succesvol verwijderd
        }
        return false; // Team niet gevonden
    }

    // Methode om een team bij te werken
    @Transactional
    public boolean updateTeam(Long id, TeamRequest teamRequest) {
        Team team = teamRepository.findById(id).orElse(null);
        if (team == null) {
            return false; // Team niet gevonden
        }

        // Werk teamgegevens bij
        team.setName(teamRequest.getName());
        team.setCountry(teamRequest.getCountry());
        team.setStadium(teamRequest.getStadium());

        // Sla het team op in de database
        teamRepository.save(team);
        return true; // Team succesvol bijgewerkt
    }
}
