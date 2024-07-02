package com.taskage.core.enitity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status_id")
    private Integer statusId;

    @Column(name = "priority_id")
    private Integer priorityId;

    @Column(name = "effort_points")
    @Nullable
    private Integer effortPoints;

    @Column(name = "estimation")
    private Integer estimation;

    @Column(name = "progress")
    private Integer progress;

    @JoinColumn(name = "sprint_id")
    @ManyToOne
    @JsonIgnore
    private Sprint sprint;

    @JoinColumn(name = "assignee_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User assignee;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "task_type_id", referencedColumnName = "id")
    private TaskType taskType;

    @JsonProperty("assigneeId")
    public Integer getAssigneeId() {
        return assignee.getId();
    }

    @JsonProperty("sprintId")
    public Integer getSprintId() {
        return sprint.getId();
    }
}
