package com.example.gachonhub.payload.request;

import com.example.gachonhub.domain.file.File;
import com.example.gachonhub.domain.question.Question;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.payload.ValidationGroups;
import com.example.gachonhub.payload.ValidationGroups.generalGroup;
import com.example.gachonhub.payload.ValidationGroups.saveGroup;
import com.example.gachonhub.payload.ValidationGroups.updateGroup;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.util.List;

@Getter
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
//    private SecondaryCategory category;
    private String category;

    @NotNull(groups = generalGroup.class, message = "내용이 누락되었습니다.")
    private String content;

    private List<MultipartFile> files;

    public Question toEntity(User user, List<File> fileList) {
        Question build = Question.builder()
                .id(this.id)
                .userId(user)
                .title(this.title)
                .category(this.category)
                .content(this.content)
                .fileList(fileList)
                .build();
        return build;
    }

}
