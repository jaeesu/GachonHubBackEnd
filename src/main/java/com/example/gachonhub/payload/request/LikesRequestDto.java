package com.example.gachonhub.payload.request;

import com.example.gachonhub.domain.comment.Comment;
import com.example.gachonhub.domain.contest.PostContest;
import com.example.gachonhub.domain.likes.Likes;
import com.example.gachonhub.domain.likes.Likes.Type;
import com.example.gachonhub.domain.question.PostQuestion;
import com.example.gachonhub.domain.user.User;
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
                .postQuestion(question)
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
