package com.example.gachonhub.domain.question;

import com.example.gachonhub.domain.comment.Comment;
import com.example.gachonhub.domain.file.File;
import com.example.gachonhub.domain.likes.Likes;
import com.example.gachonhub.domain.user.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
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

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private String title;

    private String content;

//    @ManyToOne
//    @JoinColumn(name = "category_id")
//    private SecondaryCategory category;

    private String category;

    @Column(name = "write_at")
    @Builder.Default //createdDate로 바꿔야한다.
    private Timestamp writeAt = new Timestamp(System.currentTimeMillis());

    @Builder.Default //애노테이션을 넣지 않으면 0이 들어가지 않는다.
    private Long hit = 0L;

    @OneToMany(cascade = CascadeType.PERSIST)
    List<File> fileList = new ArrayList<>();

    @OneToMany
    List<Comment> commentList = new ArrayList<>();

    @OneToMany
    List<Likes> likesList = new ArrayList<>();
}
