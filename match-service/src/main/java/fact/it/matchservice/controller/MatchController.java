package fact.it.matchservice.controller;

import fact.it.matchservice.dto.MatchRequest;
import fact.it.matchservice.dto.MatchResponse;
import fact.it.matchservice.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createMatch(@RequestBody MatchRequest matchRequest) {
        boolean result = matchService.createMatch(matchRequest); // Dit verwijst nu naar de correcte methode
        return (result ? "Match created successfully" : "Match creation failed");
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<MatchResponse> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public MatchResponse getMatchById(@PathVariable Long id) {
        return matchService.getMatchById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String updateMatchStatus(@PathVariable Long id, @RequestParam String status, @RequestParam(required = false) String score) {
        boolean result = matchService.updateMatchStatus(id, status, score);
        return (result ? "Match status updated successfully" : "Match status update failed");
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteMatch(@PathVariable Long id) {
        boolean result = matchService.deleteMatch(id);
        return (result ? "Match deleted successfully" : "Match deletion failed");
    }
}
