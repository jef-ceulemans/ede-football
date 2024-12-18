package fact.it.matchservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatchResponse {
    private Long id;             // Unieke ID van de wedstrijd
    private String team1Name;    // Naam van team 1 (opgehaald via Team-service)
    private String team2Name;    // Naam van team 2 (opgehaald via Team-service)
    private LocalDateTime matchDate; // Datum en tijd van de wedstrijd
    private String stadium;      // Stadionnaam
    private String score;        // Uitslag van de wedstrijd
    private String status;       // Status: "scheduled", "running", "finished"
}
