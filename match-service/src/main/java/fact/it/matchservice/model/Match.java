package fact.it.matchservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "match") // Database tabelnaam
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    private String team1Id; // ID van het eerste team

    private String team2Id; // ID van het tweede team

    private LocalDateTime matchDate; // Datum en tijd van de wedstrijd

    private String score; // Eindstand, bijvoorbeeld "2-1"

    private String liveScore; // Actuele stand tijdens de wedstrijd, bijvoorbeeld "1-0"

    private Integer currentMinute; // Actuele minuut van de wedstrijd

    private String status; // Status van de wedstrijd, bijvoorbeeld "scheduled", "running", "finished"

}
