package com.example.gachonhub.team.ui.dto;

import com.example.gachonhub.team.domain.Team;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.userInfo.GithubRepos;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class TeamResponseDto {

    //readme

    private Long id;
    private String name;
    private Long authorId;
    private String field;
    private Integer people;
    private String repos;
    private String type;
    private String avatarUrl;
    private String description;
    private Long commitCount;
    private boolean recruiting;
    private List<TeamMemberDto> users;
    private List<TeamReposDto> reposList;

    public static TeamResponseDto fromEntity(Team team) {

        return TeamResponseDto.builder()
                .id(team.getId())
                .name(team.getName())
                .authorId(team.getAuthorId())
                .field(team.getField())
                .people(team.getPeople())
                .repos(team.getRepos())
                .type(team.getType().name())
                .avatarUrl(team.getAvatarUrl())
                .description(team.getDescription())
                .commitCount(team.getCommitCount())
                .recruiting(team.isRecruiting())
                .users(team.getUsers().stream()
                        .map(x -> new TeamMemberDto(x.getUser()))
                        .collect(Collectors.toList()))
                .reposList(team.getReposList().stream()
                        .map(TeamReposDto::new)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    static class TeamMemberDto{
        private Long id;
        private String nickname;
        private String avatarUrl;

        public TeamMemberDto(User user) {
            this.id = user.getId();
            this.nickname = user.getNickname();
            this.avatarUrl = user.getAvatarUrl();
        }
    }

    @Getter
    static class TeamReposDto{
        private Long id;
        private String url;
        private String name;
        private String description;
        private String lang;
        private boolean main;
        private String visibility;

        public TeamReposDto(GithubRepos githubRepos) {
            this.id = githubRepos.getId();
            this.url = githubRepos.getUrl();
            this.name = githubRepos.getName();
            this.description = githubRepos.getDescription();
            this.lang = githubRepos.getLang();
            this.main = githubRepos.isMain();
            this.visibility = githubRepos.getVisibility();
        }
    }


}
