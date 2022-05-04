package com.example.gachonhub.likes.domain;

import com.example.gachonhub.comment.domain.Comment;
import com.example.gachonhub.contest.domain.PostContest;
import com.example.gachonhub.question.domain.PostQuestion;
import com.example.gachonhub.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_question_id")
    private PostQuestion postQuestionId;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment parentComment;

    @ManyToOne
    @JoinColumn(name = "post_contest_id")
    private PostContest postContest;

    public enum Type {
        QUESTION, QUESTION_COMMENT, CONTEST
    }


}
