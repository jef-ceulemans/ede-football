package fact.it.matchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LiveMatchResponse {
    private Long id;             // Unieke ID van de wedstrijd
    private String team1Name;    // Naam van team 1
    private String team2Name;    // Naam van team 2
    private String liveScore;    // Live score: bijv. "2-1"
    private String currentMinute; // Huidige minuut in de wedstrijd
    private String status;       // Status: "running", "paused", "finished"
}
