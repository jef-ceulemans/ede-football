package fact.it.teamservice.controller;

import fact.it.teamservice.dto.TeamResponse;
import fact.it.teamservice.dto.TeamRequest;
import fact.it.teamservice.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<TeamResponse> getTeams(){
        return teamService.getTeams();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public TeamResponse getTeamById(@RequestParam Long id){
        return teamService.getTeamById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String registerTeam(@RequestBody TeamRequest teamRequest){
        boolean result=teamService.registerTeam(teamRequest);
        return (result ? "Team registered" : "Team failed");
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public String deleteTeamById(@RequestParam Long id){
        boolean result = teamService.deleteTeam(id);
        return (result ? "Team deleted" : "Team not deleted");
    }



}