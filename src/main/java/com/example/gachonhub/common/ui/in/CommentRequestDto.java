package com.example.gachonhub.common.ui.in;

import com.example.gachonhub.comment.domain.Comment;
import com.example.gachonhub.question.domain.PostQuestion;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.common.ui.out.ValidationGroups.generalGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.saveGroup;
import com.example.gachonhub.common.ui.out.ValidationGroups.updateGroup;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@NoArgsConstructor
@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentRequestDto {

    @NotNull(groups = updateGroup.class, message = "아이디가 누락되었습니다.")
    @Null(groups = saveGroup.class, message = "아이디를 명시할 수 없습니다.")
    private Long id;

    @NotNull(groups = generalGroup.class, message = "내용이 누락되었습니다.")
    private String content;

    @NotNull(groups = generalGroup.class, message = "질문글 번호가 누락되었습니다.")
    private Long questionId;
    private Long parentCommentId;

    public Comment toEntity(User user, PostQuestion postQuestion, Comment parentComment) {
        return Comment.builder()
                .id(this.id)
                .userId(user)
                .content(this.content)
                .postQuestionId(postQuestion)
                .parentComment(parentComment)
                .build();
    }
}
