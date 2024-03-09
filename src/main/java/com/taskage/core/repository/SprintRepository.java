package com.taskage.core.repository;

import com.taskage.core.enitity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Integer> {
    List<Sprint> findAllByTeamId(Integer teamId);
}
