package com.taskage.core.controller;

import com.taskage.core.enitity.Team;
import com.taskage.core.repository.TeamRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
public class TeamsController {
    TeamRepository teamRepository;

    public TeamsController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping(path = "/new")
    public String addNewTeam() {
        var team = new Team();
        team.setName("Team 1");
        teamRepository.save(team);
        return "Success!";
    }
}
