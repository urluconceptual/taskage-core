package com.taskage.core.enitity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity(name = "Job_Title")
@AllArgsConstructor
public class JobTitle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "jobTitle", fetch = FetchType.LAZY)
    private Set<User> users;
}
