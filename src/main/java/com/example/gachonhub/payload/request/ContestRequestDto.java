package com.example.gachonhub.payload.request;

import com.example.gachonhub.domain.category.SubCategory;
import com.example.gachonhub.domain.contest.PostContest;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.payload.ValidationGroups.generalGroup;
import com.example.gachonhub.payload.ValidationGroups.saveGroup;
import com.example.gachonhub.payload.ValidationGroups.updateGroup;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ContestRequestDto {

    @NotNull(groups = {updateGroup.class, generalGroup.class}, message = "공모전 아이디가 누락되었습니다.")
    @Null(groups = {saveGroup.class, generalGroup.class}, message = "공모전 아이디를 명시할 수 없습니다.")
    private Long id;

    @NotNull(groups = generalGroup.class, message = "공모전 제목이 누락되었습니다.")
    private String title;

    @NotNull(groups = generalGroup.class, message = "공모전 내용이 누락되었습니다.")
    private String content;

    @NotNull(groups = generalGroup.class, message = "공모전 카테고리가 누락되었습니다.")
    private Integer category;

    @NotNull(groups = generalGroup.class, message = "공모전 이미지가 누락되었습니다.")
    private MultipartFile image;

    public PostContest toEntity(User user, SubCategory category, String url) {
        return PostContest.builder()
                .user(user)
                .title(this.title)
                .content(this.content)
                .categoryId(category)
                .image(url)
                .build();

    }

    public void updateContest(PostContest contest,  SubCategory category, String url) {
        contest.setTitle(this.title);
        contest.setCategoryId(category);
        contest.setImage(url);
    }
}
