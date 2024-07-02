package com.taskage.core.repository;

import com.taskage.core.enitity.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTitleRepository extends JpaRepository<JobTitle, Integer> {
    public JobTitle findByName(String name);
}