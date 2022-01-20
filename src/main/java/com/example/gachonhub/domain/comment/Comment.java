package com.example.gachonhub.domain.comment;

import com.example.gachonhub.domain.likes.Likes;
import com.example.gachonhub.domain.question.PostQuestion;
import com.example.gachonhub.domain.user.User;
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

    @ManyToOne
    @JoinColumn(name = "post_question_id")
    private PostQuestion postQuestionId;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @Column(name = "write_at")
    private Timestamp writeAt;

    @OneToMany
    List<Likes> likesList = new ArrayList<>();
}
