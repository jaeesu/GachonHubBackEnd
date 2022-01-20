package com.example.gachonhub.domain.question;

import com.example.gachonhub.domain.category.SubCategory;
import com.example.gachonhub.domain.comment.Comment;
import com.example.gachonhub.domain.file.UserFile;
import com.example.gachonhub.domain.likes.Likes;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.payload.request.QuestionRequestDto;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DynamicUpdate
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "post_question_id")
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private String title;

    private String content;

    @ManyToOne
    private SubCategory categoryId;

    @Column(name = "write_at")
    @Builder.Default //createdDate로 바꿔야한다.
    private Timestamp writeAt = new Timestamp(System.currentTimeMillis());

    @Builder.Default //애노테이션을 넣지 않으면 0이 들어가지 않는다.
    private Long hit = 0L;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<UserFile> userFileList = new ArrayList<>();

    @OneToMany
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany
    private List<Likes> likesList = new ArrayList<>();

    public void fromQuestionRequestWithFile(QuestionRequestDto dto, SubCategory subCategory, List<UserFile> userFileList) {
//        removeFiles();
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.categoryId = subCategory;
        this.userFileList = userFileList;
    }

    public PostQuestion removeFiles() {
//        this.fileList.clear(); //=>왜 orphan remove가 안되는지 모르겠다.
//        this.fileList.stream().forEach(m -> m.updateQuestion(null));
        this.userFileList.removeAll(this.userFileList);
        return this;
    }


}
