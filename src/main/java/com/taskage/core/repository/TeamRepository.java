package com.taskage.core.repository;

import com.taskage.core.enitity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    boolean existsByName(String name);

    Team findByName(String name);
}