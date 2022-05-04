package com.example.gachonhub.question.ui.dto;

import com.example.gachonhub.category.domain.SubCategory;
import com.example.gachonhub.file.domain.UserFile;
import com.example.gachonhub.question.domain.PostQuestion;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.common.ui.out.ValidationGroups.generalGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.saveGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.updateGroup;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionRequestDto {

    //제목, 카테고리, 내용, 첨부파일(다중 파일)

    @NotNull(groups = updateGroup.class, message = "아이디가 누락되었습니다.")
    @Null(groups = saveGroup.class, message = "아이디를 명시할 수 없습니다.")
    private Long id;

    @NotNull(groups = generalGroup.class, message = "제목이 누락되었습니다.")
    private String title;

    @NotNull(groups = generalGroup.class, message = "카테고리가 누락되었습니다.")
    private Long category;

    @NotNull(groups = generalGroup.class, message = "내용이 누락되었습니다.")
    private String content;

    private List<MultipartFile> files;

    public PostQuestion toEntity(User user, SubCategory subCategory, List<UserFile> userFileList) {
        PostQuestion build = PostQuestion.builder()
                .id(this.id)
                .userId(user)
                .title(this.title)
                .categoryId(subCategory)
                .content(this.content)
                .userFileList(userFileList)
                .build();
        return build;
    }

}
