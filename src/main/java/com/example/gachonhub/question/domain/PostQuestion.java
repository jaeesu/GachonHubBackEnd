package com.example.gachonhub.question.domain;

import com.example.gachonhub.category.domain.SubCategory;
import com.example.gachonhub.comment.domain.Comment;
import com.example.gachonhub.file.domain.UserFile;
import com.example.gachonhub.likes.domain.Likes;
import com.example.gachonhub.post.domain.Post;
import com.example.gachonhub.user.domain.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@DynamicUpdate
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostQuestion extends Post {

    @ManyToOne
    @JoinColumn(name = "category_id")
    private SubCategory categoryId;

    private Long hit = 0L;

    @OneToMany(mappedBy = "postQuestionId", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<UserFile> userFileList = new ArrayList<>();

    @OneToMany(mappedBy = "postQuestionId")
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "postQuestionId")
    private List<Likes> likesList = new ArrayList<>();

    @Builder
    public PostQuestion(Long id, User userId, String title, String content, SubCategory categoryId, Long hit, List<UserFile> userFileList, List<Comment> commentList, List<Likes> likesList) {
        super(id, userId, title, content);
        this.categoryId = categoryId;
        this.hit = hit;
        this.userFileList = userFileList;
        this.commentList = commentList;
        this.likesList = likesList;
    }
}
