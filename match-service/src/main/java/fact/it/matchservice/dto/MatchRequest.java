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
public class MatchRequest {
    private String team1Id;      // ID van het eerste team
    private String team2Id;      // ID van het tweede team
    private LocalDateTime matchDate; // Datum en tijd van de wedstrijd
    private String stadium;      // Stadion waar de wedstrijd plaatsvindt
}
