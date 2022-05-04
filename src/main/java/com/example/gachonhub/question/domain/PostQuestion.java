package com.example.gachonhub.question.domain;

import com.example.gachonhub.category.domain.SubCategory;
import com.example.gachonhub.comment.domain.Comment;
import com.example.gachonhub.common.domain.BaseTimeEntity;
import com.example.gachonhub.file.domain.UserFile;
import com.example.gachonhub.likes.domain.Likes;
import com.example.gachonhub.user.domain.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostQuestion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_question_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private SubCategory categoryId;

    @Builder.Default //애노테이션을 넣지 않으면 0이 들어가지 않는다.
    private Long hit = 0L;

    @OneToMany(mappedBy = "postQuestionId", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<UserFile> userFileList = new ArrayList<>();

    @OneToMany(mappedBy = "postQuestionId")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "postQuestionId")
    private List<Likes> likesList = new ArrayList<>();

}
