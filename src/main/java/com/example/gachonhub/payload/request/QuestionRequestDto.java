package com.example.gachonhub.payload.request;

import com.example.gachonhub.domain.category.SecondaryCategory;
import com.example.gachonhub.domain.file.File;
import com.example.gachonhub.domain.question.Question;
import com.example.gachonhub.domain.user.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionRequestDto {

    //제목, 카테고리, 내용, 첨부파일(다중 파일)

    @NotNull
    private String title;

//    @NotNull
//    private SecondaryCategory category;
    private String category;

    @NotNull
    private String content;

    private List<MultipartFile> files;

    public Question toEntity(User user, List<File> fileList) {
        Question build = Question.builder()
                .userId(user)
                .title(this.title)
                .category(this.category)
                .content(this.content)
                .fileList(fileList)
                .build();
        return build;
    }

}
