package fact.it.matchservice;

import fact.it.matchservice.dto.MatchRequest;
import fact.it.matchservice.dto.MatchResponse;
import fact.it.matchservice.model.Match;
import fact.it.matchservice.repository.MatchRepository;
import fact.it.matchservice.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceUnitTests {

    @InjectMocks
    private MatchService matchService;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(matchService, "teamServiceBaseUrl", "http://localhost:8084");
    }

    @Test
    void testCreateMatch_ValidTeams() {
        // Arrange
        MatchRequest matchRequest = new MatchRequest(1L, 2L, LocalDateTime.now(), "Camp Nou");
        when(matchRepository.save(any(Match.class))).thenAnswer(invocation -> invocation.getArgument(0));
        mockWebClientResponse(true); // Simuleert dat beide teams bestaan

        // Act
        boolean result = matchService.createMatch(matchRequest);

        // Assert
        assertTrue(result);
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    void testCreateMatch_InvalidTeams() {
        // Arrange
        MatchRequest matchRequest = new MatchRequest(1L, 99L, LocalDateTime.now(), "Camp Nou");
        mockWebClientResponse(false); // Simuleert dat team2 niet bestaat

        // Act
        boolean result = matchService.createMatch(matchRequest);

        // Assert
        assertFalse(result);
        verify(matchRepository, never()).save(any(Match.class));
    }

    @Test
    void testUpdateMatchStatus_FinishedStatus() {
        // Arrange
        Match existingMatch = Match.builder().id(1L).status("scheduled").build();
        when(matchRepository.findById(anyLong())).thenReturn(Optional.of(existingMatch));

        // Act
        boolean result = matchService.updateMatchStatus(1L, "finished", "2-1");

        // Assert
        assertTrue(result);
        assertEquals("finished", existingMatch.getStatus());
        assertEquals("2-1", existingMatch.getScore());
        verify(matchRepository, times(1)).save(existingMatch);
    }

    @Test
    void testGetAllMatches() {
        // Arrange
        Match match1 = Match.builder().id(1L).team1Id(1L).team2Id(2L).build();
        Match match2 = Match.builder().id(2L).team1Id(3L).team2Id(4L).build();
        when(matchRepository.findAll()).thenReturn(Arrays.asList(match1, match2));
        mockTeamNames();

        // Act
        List<MatchResponse> matches = matchService.getAllMatches();

        // Assert
        assertEquals(2, matches.size());
        verify(matchRepository, times(1)).findAll();
    }

    @Test
    void testGetMatchById() {
        // Arrange
        Match match = Match.builder().id(1L).team1Id(1L).team2Id(2L).build();
        when(matchRepository.findById(anyLong())).thenReturn(Optional.of(match));
        mockTeamNames();

        // Act
        MatchResponse matchResponse = matchService.getMatchById(1L);

        // Assert
        assertNotNull(matchResponse);
        assertEquals(1L, matchResponse.getId());
        verify(matchRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteMatch_Exists() {
        // Arrange
        when(matchRepository.existsById(anyLong())).thenReturn(true);

        // Act
        boolean result = matchService.deleteMatch(1L);

        // Assert
        assertTrue(result);
        verify(matchRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteMatch_NotExists() {
        // Arrange
        when(matchRepository.existsById(anyLong())).thenReturn(false);

        // Act
        boolean result = matchService.deleteMatch(1L);

        // Assert
        assertFalse(result);
        verify(matchRepository, never()).deleteById(1L);
    }

    private void mockWebClientResponse(boolean exists) {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        if (exists) {
            when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());
        } else {
            when(responseSpec.bodyToMono(Void.class)).thenThrow(new RuntimeException("Team not found"));
        }
    }

    private void mockTeamNames() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(contains("/api/team?teamId="))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(Class.class)))
                .thenAnswer(invocation -> {
                    Class<?> clazz = invocation.getArgument(0);
                    if (clazz == String.class) {
                        return Mono.just("Team Name");
                    }
                    return Mono.empty();
                });
    }
}
