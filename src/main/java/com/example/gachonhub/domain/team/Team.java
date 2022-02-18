package com.example.gachonhub.domain.team;

import com.example.gachonhub.domain.user.relation.UserToTeam;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "team")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    private String name;

    private Long authorId;

    private String field;

    private Integer people;

    private String repos; //description => readme

    @Column(name = "team_type")
    @Enumerated(EnumType.STRING)
    private TeamType type;

    private String mainImage;

    @Column(name = "commit_count")
    private Long commitCount; //d 0

    private boolean recruiting; //d 0

    private String recruitingContent;

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserToTeam> users = new HashSet<>();

    public enum TeamType {
        STUDY, CREW
    }
}
