package com.example.gachonhub.domain.group;

import com.example.gachonhub.domain.user.User;
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

    private String repos;

    @Column(name = "team_type")
    @Enumerated(EnumType.STRING)
    private TeamType type;

    private String description;

    @Column(name = "commit_count")
    private Long commitCount;

    private boolean recruiting;

    @Column(name = "recruiting_content")
    private String recruitingContent;

    private String mainImage;

    //main image

    @Builder.Default
    @OneToMany
    private Set<User> users = new HashSet<>();

    public enum TeamType {
        STUDY, CREW
    }
}
