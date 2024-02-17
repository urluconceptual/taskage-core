package com.taskage.core.enitity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity(name = "Team")
@NoArgsConstructor
public class Team {
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    public Set<User> teamMembers = new HashSet<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private Integer id;
    @Column(nullable = false, unique = true)
    private String name;
    @OneToOne
    @JoinColumn(name = "team_lead_id", referencedColumnName = "id")
    private User teamLead;

    public Team(String name) {
        this.name = name;
    }
}
