package com.example.gachonhub.payload.response;

import com.example.gachonhub.domain.team.Team;
import com.example.gachonhub.domain.user.User;
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
    private String mainImage;
    private String description;
    private Long commitCount;
    private boolean recruiting;
    private List<TeamMemberDto> users;

    public static TeamResponseDto fromEntity(Team team) {

        return TeamResponseDto.builder()
                .id(team.getId())
                .name(team.getName())
                .authorId(team.getAuthorId())
                .field(team.getField())
                .people(team.getPeople())
                .repos(team.getRepos())
                .type(team.getType().name())
                .mainImage(team.getMainImage())
                .description(team.getDescription())
                .commitCount(team.getCommitCount())
                .recruiting(team.isRecruiting())
                .users(team.getUsers().stream()
                        .map(x -> new TeamMemberDto(x.getUser()))
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


}
