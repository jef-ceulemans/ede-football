package fact.it.teamservice;

import fact.it.teamservice.dto.TeamRequest;
import fact.it.teamservice.dto.TeamResponse;
import fact.it.teamservice.model.Team;
import fact.it.teamservice.repository.TeamRepository;
import fact.it.teamservice.service.TeamService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceUnitTests {

    @InjectMocks
    private TeamService teamService;

    @Mock
    private TeamRepository teamRepository;

    @Test
    void testGetTeams() {
        // Arrange
        Team team1 = new Team(1L, "FC Barcelona", "Spain", "Camp Nou");
        Team team2 = new Team(2L, "Manchester United", "England", "Old Trafford");
        when(teamRepository.findAll()).thenReturn(Arrays.asList(team1, team2));

        // Act
        List<TeamResponse> teams = teamService.getTeams();

        // Assert
        assertEquals(2, teams.size());
        assertEquals("FC Barcelona", teams.get(0).getName());
        assertEquals("Manchester United", teams.get(1).getName());
        verify(teamRepository, times(1)).findAll();
    }

    @Test
    void testGetTeamById_TeamExists() {
        // Arrange
        Team team = new Team(1L, "FC Barcelona", "Spain", "Camp Nou");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));

        // Act
        TeamResponse teamResponse = teamService.getTeamById(1L);

        // Assert
        assertNotNull(teamResponse);
        assertEquals("FC Barcelona", teamResponse.getName());
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTeamById_TeamDoesNotExist() {
        // Arrange
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        TeamResponse teamResponse = teamService.getTeamById(1L);

        // Assert
        assertNull(teamResponse);
        verify(teamRepository, times(1)).findById(1L);
    }

    @Test
    void testRegisterTeam() {
        // Arrange
        TeamRequest teamRequest = new TeamRequest("FC Barcelona", "Spain", "Camp Nou");
        Team team = Team.builder()
                .name("FC Barcelona")
                .country("Spain")
                .stadium("Camp Nou")
                .build();

        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // Act
        boolean result = teamService.registerTeam(teamRequest);

        // Assert
        assertTrue(result);
        verify(teamRepository, times(1)).save(any(Team.class));
    }

    @Test
    void testDeleteTeam_TeamExists() {
        // Arrange
        when(teamRepository.existsById(1L)).thenReturn(true);

        // Act
        boolean result = teamService.deleteTeam(1L);

        // Assert
        assertTrue(result);
        verify(teamRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTeam_TeamDoesNotExist() {
        // Arrange
        when(teamRepository.existsById(1L)).thenReturn(false);

        // Act
        boolean result = teamService.deleteTeam(1L);

        // Assert
        assertFalse(result);
        verify(teamRepository, never()).deleteById(1L);
    }

    @Test
    void testUpdateTeam_TeamExists() {
        // Arrange
        Team team = new Team(1L, "FC Barcelona", "Spain", "Camp Nou");
        TeamRequest teamRequest = new TeamRequest("Updated FC Barcelona", "Updated Spain", "Updated Camp Nou");
        when(teamRepository.findById(1L)).thenReturn(Optional.of(team));
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // Act
        boolean result = teamService.updateTeam(1L, teamRequest);

        // Assert
        assertTrue(result);
        assertEquals("Updated FC Barcelona", team.getName());
        assertEquals("Updated Spain", team.getCountry());
        assertEquals("Updated Camp Nou", team.getStadium());
        verify(teamRepository, times(1)).save(team);
    }

    @Test
    void testUpdateTeam_TeamDoesNotExist() {
        // Arrange
        TeamRequest teamRequest = new TeamRequest("Updated FC Barcelona", "Updated Spain", "Updated Camp Nou");
        when(teamRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        boolean result = teamService.updateTeam(1L, teamRequest);

        // Assert
        assertFalse(result);
        verify(teamRepository, never()).save(any(Team.class));
    }
}
