package com.taskage.core.repository;

import com.taskage.core.enitity.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Integer> {
    @Query("SELECT s FROM Sprint s WHERE s.team.id = ?1")
    List<Sprint> findAllByTeamId(Integer teamId);
}
