package com.taskage.core.service;

import com.taskage.core.dto.team.TeamCreateRequestDto;
import com.taskage.core.dto.team.TeamResponseDto;
import com.taskage.core.enitity.Team;
import com.taskage.core.enitity.User;
import com.taskage.core.exception.notFound.UserNotFoundException;
import com.taskage.core.mapper.TeamMapper;
import com.taskage.core.repository.TeamRepository;
import com.taskage.core.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TeamService {
    private final UserService userService;
    private final TeamRepository teamRepository;
    private TeamMapper teamMapper;

    public void create(TeamCreateRequestDto teamCreateRequestDto) throws UserNotFoundException {
        Team newTeam = teamMapper.mapTeamCreateRequestDtoToTeam(teamCreateRequestDto);

        teamRepository.save(newTeam);
        userService.assignTeamToAll(teamCreateRequestDto.teamMemberIds(), newTeam);
    }

    public List<TeamResponseDto> getAll() {
        return teamRepository.findAll().stream().map(teamMapper::mapTeamToTeamResponseDto).toList();
    }
}
