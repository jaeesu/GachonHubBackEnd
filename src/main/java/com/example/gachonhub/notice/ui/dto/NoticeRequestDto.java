package com.example.gachonhub.notice.ui.dto;

import com.example.gachonhub.notice.domain.PostNotice;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.common.ui.out.ValidationGroups.generalGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.saveGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.updateGroup;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NoticeRequestDto {

    @NotNull(groups = updateGroup.class, message = "아이디가 누락되었습니다.")
    @Null(groups = saveGroup.class, message = "아이디를 명시할 수 없습니다.")
    private Long id;

    @NotNull(groups = generalGroup.class, message = "제목이 누락되었습니다.")
    private String title;

    @NotNull(groups = generalGroup.class, message = "내용이 누락되었습니다.")
    private String content;

    public PostNotice toEntity(User user) {
        return PostNotice.builder()
                .id(this.id)
                .userId(user)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
