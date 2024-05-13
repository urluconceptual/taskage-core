package com.taskage.core.service;

import com.taskage.core.dto.sprint.SprintCreateRequestDto;
import com.taskage.core.dto.sprint.SprintUpdateRequestDto;
import com.taskage.core.enitity.Sprint;
import com.taskage.core.exception.notFound.NotFoundException;
import com.taskage.core.mapper.SprintMapper;
import com.taskage.core.repository.SprintRepository;
import com.taskage.core.repository.TeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SprintService {
    private final SprintRepository sprintRepository;
    private final SprintMapper sprintMapper;
    private final TeamRepository teamRepository;

    public List<Sprint> getAllForTeam(Integer teamId) {
        return sprintRepository.findAllByTeamId(teamId);
    }

    public void create(SprintCreateRequestDto sprintCreateRequestDto) {
        Sprint newSprint = sprintMapper.mapSprintCreateRequestDtoToSprint(sprintCreateRequestDto);

        newSprint.setTeam(teamRepository.findById(sprintCreateRequestDto.teamId())
                .orElseThrow(() -> new NotFoundException("Team " + sprintCreateRequestDto.teamId() + " not found")));

        sprintRepository.save(newSprint);
    }

    public void update(SprintUpdateRequestDto sprintUpdateRequestDto) {
        Sprint sprint = sprintRepository.findById(sprintUpdateRequestDto.id())
                .orElseThrow(() -> new NotFoundException("Sprint " + sprintUpdateRequestDto.id() + " not found."));

        sprint.setStartDate(sprintUpdateRequestDto.startDate());
        sprint.setEndDate(sprintUpdateRequestDto.endDate());

        sprintRepository.save(sprint);
    }

    public void delete(Integer id) {
        sprintRepository.deleteById(id);
    }
}
