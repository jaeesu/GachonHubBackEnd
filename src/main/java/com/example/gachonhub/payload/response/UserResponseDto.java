package com.example.gachonhub.payload.response;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.relation.UserToTeam;
import com.example.gachonhub.domain.user.userInfo.UserRepos;
import com.example.gachonhub.domain.user.userInfo.UserSns;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
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
                .createdAt(user.getCreatedAt())
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

        public UserSnsDto(UserSns sns) {
            this.id = sns.getId();
            this.category = sns.getCategory();
            this.url = sns.getUrl();
        }
    }

    @Getter
    public static class UserReposDto {
        private Long id;
        private String url;
        private String title;
        private String description;
        private String lang;
        private LocalDateTime updatedAt;

        public UserReposDto(UserRepos repos) {
            this.id = repos.getId();
            this.url = repos.getUrl();
            this.title = repos.getTitle();
            this.description = repos.getDescription();
            this.lang = repos.getLang();
            this.updatedAt = repos.getUpdatedAt();
        }
    }

    @Getter
    public static class UserGroupDto {
        private Long id;
        private String name;
        private String mainImage;

        public UserGroupDto(UserToTeam team) {
            this.id = team.getTeam().getId();
            this.name = team.getTeam().getName();
            this.mainImage = team.getTeam().getMainImage();
        }
    }

}
