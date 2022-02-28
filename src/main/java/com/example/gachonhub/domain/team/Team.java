package com.example.gachonhub.domain.team;

import com.example.gachonhub.domain.user.relation.UserToTeam;
import com.example.gachonhub.domain.user.userInfo.GithubRepos;
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

    @Setter
    private String name;

    private Long authorId;

    @Setter
    private String field;

    @Setter
    private Integer people;

    @Setter
    private String repos; //description => readme

    @Setter
    private String description;

    @Column(name = "team_type")
    @Enumerated(EnumType.STRING)
    private TeamType type;

    @Setter
    private String avatarUrl;

    private Long commitCount; //d 0

    @Setter
    private boolean recruiting; //d 0

    @Setter
    private String recruitingContent;

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserToTeam> users = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<GithubRepos> reposList = new HashSet<>();

    public enum TeamType {
        STUDY, CREW
    }

}
