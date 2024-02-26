package com.taskage.core.enitity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "Team")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    private Set<User> users = new HashSet<>();

    public Team(Integer id) {
        this.id = id;
    }

    public Team(String name) {
        this.name = name;
    }
}
