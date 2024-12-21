package fact.it.matchservice.service;

import fact.it.matchservice.dto.MatchRequest;
import fact.it.matchservice.dto.MatchResponse;
import fact.it.matchservice.dto.TeamResponse;
import fact.it.matchservice.model.Match;
import fact.it.matchservice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import jakarta.annotation.PostConstruct;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final WebClient webClient;

    @Value("${teamservice.baseurl}")
    private String teamServiceBaseUrl;

    // Methode om voorbeelddata toe te voegen bij de opstart
    @PostConstruct
    public void loadData() {
        if (matchRepository.count() <= 0) {
            // Voorbeeldwedstrijd 1
            Match match1 = Match.builder()
                    .team1Id(1L) // Vervang met daadwerkelijke team IDs als ze bestaan
                    .team2Id(2L) // Vervang met daadwerkelijke team IDs
                    .matchDate(LocalDateTime.of(2024, 12, 20, 18, 0)) // Datum en tijd van de wedstrijd
                    .stadium("Camp Nou") // Stadionnaam
                    .score("0-0") // Begin score
                    .status("scheduled") // Status van de wedstrijd
                    .build();

            // Voorbeeldwedstrijd 2
            Match match2 = Match.builder()
                    .team1Id(1L) // Vervang met daadwerkelijke team IDs
                    .team2Id(3L) // Vervang met daadwerkelijke team IDs
                    .matchDate(LocalDateTime.of(2024, 12, 22, 20, 0)) // Datum en tijd van de wedstrijd
                    .stadium("Old Trafford") // Stadionnaam
                    .score("0-0") // Begin score
                    .status("scheduled") // Status van de wedstrijd
                    .build();

            matchRepository.save(match1);
            matchRepository.save(match2);
        }
    }

    @Transactional
    public boolean createMatch(MatchRequest matchRequest) {
        Match match = Match.builder()
                .team1Id(matchRequest.getTeam1Id())
                .team2Id(matchRequest.getTeam2Id())
                .stadium(matchRequest.getStadium())
                .score("0-0") // Startscore is 0-0 (of leeg, afhankelijk van je voorkeur)
                .status("scheduled")
                .build();

        // Validating if teams exist
        boolean team1Exists = validateTeamExists(matchRequest.getTeam1Id());
        boolean team2Exists = validateTeamExists(matchRequest.getTeam2Id());

        if (team1Exists && team2Exists) {
            matchRepository.save(match);
            return true;
        } else {
            return false; // One or both teams do not exist
        }
    }

    public boolean updateMatchStatus(Long id, String status, String score) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found"));
        // Als de status is 'finished', kan de score worden bijgewerkt
        if ("finished".equalsIgnoreCase(status)) {
            match.setScore(score); // Score instellen wanneer de wedstrijd is afgelopen
        }
        match.setStatus(status);
        matchRepository.save(match);
        return true;
    }

    public List<MatchResponse> getAllMatches() {
        List<Match> matches = matchRepository.findAll();

        return matches.stream()
                .map(this::mapToMatchResponse)
                .collect(Collectors.toList());
    }

    public MatchResponse getMatchById(Long matchId) {
        Match match = matchRepository.findById(matchId)
                .orElseThrow(() -> new RuntimeException("Match not found"));

        return mapToMatchResponse(match);
    }



    private boolean validateTeamExists(Long teamId) {
        try {
            webClient.get()
                    .uri("http://" + teamServiceBaseUrl + "/api/team/" + teamId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
            return true; // If the call succeeds, the team exists
        } catch (Exception e) {
            return false; // If the call fails, the team does not exist
        }
    }

    /*private String getTeamName(Long teamId) {
        return webClient.get()
                .uri("http://" + teamServiceBaseUrl + "/api/team?teamId=" + teamId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }*/

    private String getTeamName(Long teamId) {
        try {
            TeamResponse teamResponse = webClient.get()
                    .uri("http://" + teamServiceBaseUrl + "/api/team?teamId=" + teamId)
                    .retrieve()
                    .bodyToMono(TeamResponse.class)
                    .block();

            System.out.println("Team ID: " + teamId + ", Name: " + (teamResponse != null ? teamResponse.getName() : "null"));
            return teamResponse != null ? teamResponse.getName() : "Unknown";
        } catch (Exception e) {
            System.err.println("Error fetching team with ID: " + teamId + " - " + e.getMessage());
            return "Unknown";
        }
    }



    public boolean deleteMatch(Long id) {
        if (matchRepository.existsById(id)) {
            matchRepository.deleteById(id);
            return true;
        }
        return false;
    }



    private MatchResponse mapToMatchResponse(Match match) {
        return MatchResponse.builder()
                .id(match.getId())
                .team1Name(getTeamName(match.getTeam1Id()))
                .team2Name(getTeamName(match.getTeam2Id()))
                .matchDate(match.getMatchDate())
                .stadium(match.getStadium())
                .score(match.getScore())
                .status(match.getStatus())
                .build();
    }
}
