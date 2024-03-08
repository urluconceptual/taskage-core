package com.taskage.core.service;

import com.taskage.core.dto.team.TeamResponseDto;
import com.taskage.core.dto.team.TeamSaveRequestDto;
import com.taskage.core.enitity.Team;
import com.taskage.core.exception.notFound.NotFoundException;
import com.taskage.core.mapper.TeamMapper;
import com.taskage.core.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeamService {
    private final UserService userService;
    private final TeamRepository teamRepository;
    private TeamMapper teamMapper;

    public void create(TeamSaveRequestDto teamSaveRequestDto) throws NotFoundException {
        Team newTeam = teamMapper.mapTeamSaveRequestDtoToTeam(teamSaveRequestDto);

        teamRepository.save(newTeam);
        userService.assignTeamToAll(teamSaveRequestDto.teamMemberIds(), newTeam);
    }

    public void update(TeamSaveRequestDto teamSaveRequestDto) throws NotFoundException {
        var team = teamRepository.findById(teamSaveRequestDto.id())
                .orElseThrow(() -> new NotFoundException("Team not found"));
        team.setName(teamSaveRequestDto.name());
        teamRepository.save(team);
        removeAllUsersFromTeam(team.getId());
        userService.assignTeamToAll(teamSaveRequestDto.teamMemberIds(), team);
    }

    public void delete(int teamId) {
        removeAllUsersFromTeam(teamId);
        teamRepository.deleteById(teamId);
    }

    public void removeAllUsersFromTeam(int teamId) {
        var team = teamRepository.findById(teamId).orElseThrow(() -> new NotFoundException("Team not found"));
        team.getUsers().forEach(user -> user.setTeam(null));
    }

    public List<TeamResponseDto> getAll() {
        return teamRepository.findAll().stream().map(teamMapper::mapTeamToTeamResponseDto).toList();
    }
}
