package com.example.gachonhub.comment.domain;

import com.example.gachonhub.likes.domain.Likes;
import com.example.gachonhub.question.domain.PostQuestion;
import com.example.gachonhub.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private String content;

    @ManyToOne
    @JoinColumn(name = "post_question_id")
    private PostQuestion postQuestionId;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Builder.Default
    @Column(name = "write_at")
    private Timestamp writeAt = new Timestamp(System.currentTimeMillis());

    @OneToMany(mappedBy = "parentComment")
    List<Likes> likesList = new ArrayList<>();
}
