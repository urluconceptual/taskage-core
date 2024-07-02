package com.taskage.core.service;

import com.taskage.core.dto.sprint.SprintCreateRequestDto;
import com.taskage.core.dto.sprint.SprintUpdateRequestDto;
import com.taskage.core.enitity.Sprint;
import com.taskage.core.exception.notFound.NotFoundException;
import com.taskage.core.mapper.SprintMapper;
import com.taskage.core.repository.SprintRepository;
import com.taskage.core.repository.TeamRepository;
import com.taskage.core.utils.UserActivityLogger;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SprintService {
    private final SprintRepository sprintRepository;
    private final SprintMapper sprintMapper;
    private final TeamRepository teamRepository;
    private final UserActivityLogger userActivityLogger;

    @NotNull
    public List<Sprint> getAllForTeam(@NotNull Integer teamId) {
        return sprintRepository.findAllByTeamId(teamId);
    }

    @NotNull
    @Transactional
    public Sprint create(@NotNull SprintCreateRequestDto sprintCreateRequestDto) {
        Sprint newSprint = sprintMapper.mapSprintCreateRequestDtoToSprint(sprintCreateRequestDto);

        newSprint.setTeam(teamRepository.findById(sprintCreateRequestDto.teamId())
                .orElseThrow(() -> new NotFoundException("Team " + sprintCreateRequestDto.teamId() + " not found")));

        sprintRepository.save(newSprint);
        userActivityLogger.logUserActivity("Sprint created with id " + newSprint.getId(), "INFO");
        return newSprint;
    }

    @NotNull
    @Transactional
    public Sprint update(@NotNull SprintUpdateRequestDto sprintUpdateRequestDto) {
        Sprint sprint = sprintRepository.findById(sprintUpdateRequestDto.id())
                .orElseThrow(() -> new NotFoundException("Sprint " + sprintUpdateRequestDto.id() + " not found."));

        sprint.setStartDate(sprintUpdateRequestDto.startDate());
        sprint.setEndDate(sprintUpdateRequestDto.endDate());

        sprintRepository.save(sprint);
        userActivityLogger.logUserActivity("Sprint updated with id " + sprint.getId(), "INFO");
        return sprint;
    }

    @Transactional
    public void delete(@NotNull Integer id) {
        sprintRepository.deleteById(id);
        userActivityLogger.logUserActivity("Sprint deleted with id " + id, "INFO");
    }
}
