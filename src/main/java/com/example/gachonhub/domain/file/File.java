package com.example.gachonhub.domain.file;

import com.example.gachonhub.domain.question.Question;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "user_file")
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question questionId;

    @Lob
    private byte[] image;

}
