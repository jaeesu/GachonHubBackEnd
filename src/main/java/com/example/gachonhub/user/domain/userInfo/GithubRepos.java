package com.example.gachonhub.user.domain.userInfo;

import com.example.gachonhub.team.domain.Team;
import com.example.gachonhub.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Builder
@Table(name = "github_repository")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GithubRepos {

    @Id
    @Column(name = "github_repository_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    private String url;

    private String name;

    private String fullName;

    private String description;

    private String lang;

    private boolean main;

    private String visibility;

    private Timestamp updatedAt;

    public void removeMain() {
        this.main = false;
    }

    public void addMain() {
        this.main = true;
    }
}
