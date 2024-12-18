package fact.it.matchservice.service;

import fact.it.matchservice.dto.MatchRequest;
import fact.it.matchservice.dto.MatchResponse;
import fact.it.matchservice.model.Match;
import fact.it.matchservice.repository.MatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final WebClient webClient;

    @Value("${teamservice.baseurl}")
    private String teamServiceBaseUrl;

    public boolean createMatch(MatchRequest matchRequest) {
        Match match = Match.builder()
                .team1Id(matchRequest.getTeam1Id())
                .team2Id(matchRequest.getTeam2Id())
                .matchDate(matchRequest.getMatchDate())
                .stadium(matchRequest.getStadium())
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

    public boolean updateMatchStatus(Long id, String status) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found"));
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

    private String getTeamName(Long teamId) {
        return webClient.get()
                .uri("http://" + teamServiceBaseUrl + "/api/team?teamId=" + teamId)
                .retrieve()
                .bodyToMono(String.class)
                .block();
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
