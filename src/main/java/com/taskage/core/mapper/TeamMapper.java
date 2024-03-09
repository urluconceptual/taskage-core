package com.taskage.core.mapper;

import com.taskage.core.dto.team.TeamResponseDto;
import com.taskage.core.dto.team.TeamSaveRequestDto;
import com.taskage.core.enitity.Team;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class TeamMapper {
    public Team mapTeamSaveRequestDtoToTeam(TeamSaveRequestDto teamSaveRequestDto) {
        return new Team(
                teamSaveRequestDto.name()
        );
    }

    public TeamResponseDto mapTeamToTeamResponseDto(Team team) {
        return new TeamResponseDto(
                team.getId(),
                team.getName(),
                team.getUsers().stream().filter(user -> user.getAuthRole().equals("ROLE_MANAGER")).findFirst().get()
                    .getId()
        );
    }
}
