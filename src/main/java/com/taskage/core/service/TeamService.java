package com.taskage.core.service;

import com.taskage.core.dto.team.TeamCreateRequestDto;
import com.taskage.core.enitity.Team;
import com.taskage.core.enitity.User;
import com.taskage.core.exception.notFound.UserNotFoundException;
import com.taskage.core.mapper.TeamMapper;
import com.taskage.core.repository.TeamRepository;
import com.taskage.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeamService {
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private TeamMapper teamMapper;

    public void create(TeamCreateRequestDto teamCreateRequestDto) throws UserNotFoundException {
        Team newTeam = teamMapper.mapTeamCreateRequestDtoToTeam(teamCreateRequestDto);

        User teamLead =
                userRepository.findById(teamCreateRequestDto.teamLeadId()).orElseThrow(UserNotFoundException::new);
        newTeam.setTeamLead(teamLead);

        teamRepository.save(newTeam);
    }
}
