package com.example.gachonhub.likes.ui.dto;

import com.example.gachonhub.comment.domain.Comment;
import com.example.gachonhub.contest.domain.PostContest;
import com.example.gachonhub.likes.domain.Likes;
import com.example.gachonhub.likes.domain.Likes.Type;
import com.example.gachonhub.question.domain.PostQuestion;
import com.example.gachonhub.user.domain.User;
import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LikesRequestDto {

    @NotNull(message = "상위 타입이 누락되었습니다.")
    private Type type;

    @NotNull(message = "상위 아이디가 누락되었습니다.")
    private Long id;


    public Likes toEntity(User user, PostQuestion question) {
        return Likes.builder()
                .user(user)
                .postQuestionId(question)
                .build();
    }

    public Likes toEntity(User user, Comment comment) {
        return Likes.builder()
                .user(user)
                .parentComment(comment)
                .build();
    }

    public Likes toEntity(User user, PostContest contest) {
        return Likes.builder()
                .user(user)
                .postContest(contest)
                .build();
    }
}
