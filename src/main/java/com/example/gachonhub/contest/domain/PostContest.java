package com.example.gachonhub.contest.domain;

import com.example.gachonhub.category.domain.SubCategory;
import com.example.gachonhub.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Builder
@Table(name = "post_contest")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostContest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_contest_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    private String title;
    @Setter
    private String content;

    @Setter
    @ManyToOne
    @JoinColumn(name = "category_id")
    private SubCategory categoryId;

    private Integer hit;

    @Builder.Default
    private Timestamp writeAt = new Timestamp(System.currentTimeMillis());

    @Setter
    private String image;

}
