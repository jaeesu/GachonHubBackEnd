package com.example.gachonhub.payload.request;

import com.example.gachonhub.domain.user.User;
import lombok.*;

import javax.validation.constraints.NotNull;
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
        @NotNull(message = "sns 카테고리가 누락되었습니다.")
        private String category;
        @NotNull(message = "sns 주소가 누락되었습니다.")
        private String url;
    }

    public void toEntity(User user, UserInfoRequestDto dto) {
        user.setMajor(dto.getMajor());
        user.setGraduate(dto.isGraduate());
    }
}
