package com.example.gachonhub.domain.likes;

import com.example.gachonhub.domain.comment.Comment;
import com.example.gachonhub.domain.contest.PostContest;
import com.example.gachonhub.domain.question.PostQuestion;
import com.example.gachonhub.domain.user.User;
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
    private PostQuestion postQuestion;

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
