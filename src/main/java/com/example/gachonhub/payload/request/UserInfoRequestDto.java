package com.example.gachonhub.payload.request;

import com.example.gachonhub.domain.user.User;
import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoRequestDto {

    private String major;
    private boolean graduate;
    private List<UserSnsDto> sns;

    @Getter
    public static class UserSnsDto {
        private Long id;
        private String category;
        private String url;
    }

    public User toEntity(User user) {
        return User.builder()
                .id(user.getId())
                .nickname(user.getNickname())
                .name(user.getName())
                .avatarUrl(user.getAvatarUrl())
                .company(user.getCompany())
                .description(user.getDescription())
                .major(this.getMajor())
                .graduate(this.isGraduate())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .githubToken(user.getGithubToken())
                .build();
    }
}
