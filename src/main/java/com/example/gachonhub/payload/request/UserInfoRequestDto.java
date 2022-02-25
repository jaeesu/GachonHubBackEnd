package com.example.gachonhub.payload.request;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.userInfo.UserSns;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInfoRequestDto {

    private String major;
    private boolean graduate;
    private List<UserSnsDto> sns;

    @Size(max = 3, message = "대표 레포지토리는 3개까지 지정할 수 있습니다.")
    private List<Long> repos;

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
        if (dto.getSns().size() > 0 && dto.getSns() != null) {
            user.getSns().clear();
            dto.getSns().stream()
                    .map(x ->
                            UserSns.builder()
                                    .id(x.getId())
                                    .userId(user)
                                    .category(x.getCategory())
                                    .url(x.getUrl())
                                    .build()
                    ).forEach(x -> user.getSns().add(x));
        }
    }
}
