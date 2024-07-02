package com.taskage.core.service;

import com.taskage.core.dto.team.TeamResponseDto;
import com.taskage.core.dto.team.TeamSaveRequestDto;
import com.taskage.core.enitity.Team;
import com.taskage.core.exception.conflict.TeamNameConflictException;
import com.taskage.core.exception.notFound.NotFoundException;
import com.taskage.core.mapper.TeamMapper;
import com.taskage.core.repository.TeamRepository;
import com.taskage.core.utils.UserActivityLogger;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {
    private final UserService userService;
    private final TeamRepository teamRepository;
    private final TeamMapper teamMapper;
    private final UserActivityLogger userActivityLogger;

    @NotNull
    public TeamResponseDto create(@NotNull TeamSaveRequestDto teamSaveRequestDto) throws NotFoundException {
        if (teamRepository.existsByName(teamSaveRequestDto.name())) {
            throw new TeamNameConflictException();
        }
        Team newTeam = teamMapper.mapTeamSaveRequestDtoToTeam(teamSaveRequestDto);

        teamRepository.save(newTeam);
        userService.assignTeamToAll(teamSaveRequestDto.teamMemberIds(), newTeam);

        userActivityLogger.logUserActivity("Team created with name " + newTeam.getName(), "INFO");
        return teamMapper.mapTeamSaveRequestDtoToTeamResponseDto(newTeam.getId(), teamSaveRequestDto);
    }

    @NotNull
    public TeamResponseDto update(@NotNull TeamSaveRequestDto teamSaveRequestDto) throws NotFoundException {
        var team = teamRepository.findById(teamSaveRequestDto.id())
                .orElseThrow(() -> new NotFoundException("Team not found"));
        team.setName(teamSaveRequestDto.name());
        teamRepository.save(team);
        removeAllUsersFromTeam(team.getId());
        userService.assignTeamToAll(teamSaveRequestDto.teamMemberIds(), team);
        userActivityLogger.logUserActivity("Team updated with id " + team.getId(), "INFO");
        return teamMapper.mapTeamSaveRequestDtoToTeamResponseDto(team.getId(), teamSaveRequestDto);
    }

    public void removeAllUsersFromTeam(int teamId) {
        var team = teamRepository.findById(teamId).orElseThrow(() -> new NotFoundException("Team not found"));
        team.getUsers().forEach(user -> user.setTeam(null));
        userActivityLogger.logUserActivity("All users removed from team with id " + teamId, "INFO");
    }

    public void delete(int teamId) {
        removeAllUsersFromTeam(teamId);
        teamRepository.deleteById(teamId);
        userActivityLogger.logUserActivity("Team deleted with id " + teamId, "INFO");
    }

    @NotNull
    public List<TeamResponseDto> getAll() {
        return teamRepository.findAll().stream().map(teamMapper::mapTeamToTeamResponseDto).toList();
    }
}
