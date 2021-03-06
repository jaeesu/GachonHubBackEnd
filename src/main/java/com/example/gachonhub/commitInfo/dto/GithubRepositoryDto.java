package com.example.gachonhub.commitInfo.dto;

import com.example.gachonhub.team.domain.Team;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.userInfo.GithubRepos;
import lombok.*;

import java.sql.Timestamp;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class GithubRepositoryDto {

    private Long id;
    private String name;
    private String full_name;
    private String description;
    private String html_url;
    private String visibility;
    private Timestamp updated_at;

    public GithubRepos toEntity(User user) {
        return GithubRepos.builder()
                .id(this.id)
                .user(user)
                .url(this.html_url)
                .name(this.name)
                .fullName(this.full_name)
                .description(this.description)
                .main(false)
                .visibility(this.visibility)
                .updatedAt(this.updated_at)
                .build();
    }

    public GithubRepos toEntity(Team team) {
        return GithubRepos.builder()
                .id(this.id)
                .team(team)
                .url(this.html_url)
                .name(this.name)
                .fullName(this.full_name)
                .description(this.description)
                .main(false)
                .visibility(this.visibility)
                .updatedAt(this.updated_at)
                .build();
    }

}
