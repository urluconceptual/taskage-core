package com.taskage.core.controller;

import com.taskage.core.enitity.Team;
import com.taskage.core.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/teams")
public class TeamsController {
    TeamRepository teamRepository;

    @GetMapping(path = "/new")
    public String addNewTeam() {
        var team = new Team();
        team.setName("Team 1");
        teamRepository.save(team);
        return "Success!";
    }
}
