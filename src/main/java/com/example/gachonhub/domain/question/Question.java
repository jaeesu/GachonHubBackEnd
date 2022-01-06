package com.example.gachonhub.domain.question;

import com.example.gachonhub.domain.category.SecondaryCategory;
import com.example.gachonhub.domain.comment.Comment;
import com.example.gachonhub.domain.file.File;
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
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "question_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User userId;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private SecondaryCategory category;

    @Column(name = "write_at")
    private Timestamp writeAt;

    private Integer hit;

    @OneToMany(cascade = CascadeType.PERSIST)
    List<File> fileList = new ArrayList<>();

    @OneToMany
    List<Comment> commentList = new ArrayList<>();

    @OneToMany
    List<Likes> likesList = new ArrayList<>();
}
