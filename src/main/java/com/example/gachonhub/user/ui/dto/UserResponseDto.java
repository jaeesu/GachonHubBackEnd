package com.example.gachonhub.user.ui.dto;

import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserToTeam;
import com.example.gachonhub.user.domain.userInfo.GithubRepos;
import com.example.gachonhub.user.domain.userInfo.UserSns;
import lombok.Builder;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
public class UserResponseDto {

    private Long id;
    private String nickname;
    private String name;
    private String avatarUrl;
    private String company;
    private String description;
    private String major;
    private boolean graduate;
    private String role;
    private String createdAt;
    private Set<UserSnsDto> sns;
    private Set<UserReposDto> repos;
    private Set<UserGroupDto> groups;

    public static UserResponseDto fromEntity(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .company(user.getCompany())
                .description(user.getDescription())
                .major(user.getMajor())
                .graduate(user.getGraduate())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt().toString())
                .sns(user.getSns().stream()
                        .map(UserSnsDto::new).collect(Collectors.toSet()))
                .repos(user.getRepos().stream()
                        .map(UserReposDto::new).collect(Collectors.toSet()))
                .groups(user.getGroups().stream()
                        .map(UserGroupDto::new).collect(Collectors.toSet()))
                .build();
    }

    @Getter
    public static class UserSnsDto {
        private Long id;
        private String category;
        private String url;
        private boolean auto;

        public UserSnsDto(UserSns sns) {
            this.id = sns.getId();
            this.category = sns.getCategory();
            this.url = sns.getUrl();
            this.auto = sns.isAuto();
        }
    }

    @Getter
    public static class UserReposDto {
        private Long id;
        private String url;
        private String name;
        private String fullName;
        private String description;
        private String lang;
        private boolean main;
        private String updatedAt;

        public UserReposDto(GithubRepos repos) {
            this.id = repos.getId();
            this.url = repos.getUrl();
            this.name = repos.getName();
            this.fullName = repos.getFullName();
            this.description = repos.getDescription();
            this.main = repos.isMain();
            this.lang = repos.getLang();
            this.updatedAt = repos.getUpdatedAt().toString();
        }
    }

    @Getter
    public static class UserGroupDto {
        private Long id;
        private String name;
        private String mainImage;
        private String type;

        public UserGroupDto(UserToTeam team) {
            this.id = team.getTeam().getId();
            this.name = team.getTeam().getName();
            this.mainImage = team.getTeam().getAvatarUrl();
            this.type = team.getTeam().getType().name();
        }
    }

}
