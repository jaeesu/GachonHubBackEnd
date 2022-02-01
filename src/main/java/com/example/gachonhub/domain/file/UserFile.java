package com.example.gachonhub.domain.file;

import com.example.gachonhub.domain.question.PostQuestion;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserFile {

    @Id
    @Column(name = "user_file_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_question_id")
    private PostQuestion postQuestionId;

    private String realName;

    private String imageUrl;

    public void updateQuestion(PostQuestion postQuestion) {
        this.postQuestionId = postQuestion;
    }

}
