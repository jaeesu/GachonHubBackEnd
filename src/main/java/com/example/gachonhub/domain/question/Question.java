package com.example.gachonhub.domain.question;

import com.example.gachonhub.domain.category.SecondaryCategory;
import com.example.gachonhub.domain.comment.Comment;
import com.example.gachonhub.domain.likes.Likes;
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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private SecondaryCategory category;

    private String title;

    private String content;

    @Column(name = "write_at")
    private Timestamp writeAt;

    private Integer hit;

    @OneToMany
    List<Comment> commentList = new ArrayList<>();

    @OneToMany
    List<Likes> likesList = new ArrayList<>();
}
