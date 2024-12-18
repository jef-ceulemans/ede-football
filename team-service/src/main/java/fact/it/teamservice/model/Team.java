package fact.it.teamservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import jakarta.persistence.*;

@Entity
@Table(name = "team")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder // Voeg deze annotatie toe

public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long teamId;

    private String name;
    private String country;
    private String stadium;
}
