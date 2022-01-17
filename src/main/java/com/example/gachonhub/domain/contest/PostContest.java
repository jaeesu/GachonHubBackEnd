package com.example.gachonhub.domain.contest;

import com.example.gachonhub.domain.category.SecondaryCategory;
import com.example.gachonhub.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostContest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contest_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String name;

    private String content;

//    @ManyToOne
//    @JoinColumn(name = "category")
    private String category;

    private Integer people;

    private Date start;

    private Date end;

    private Timestamp timestamp;

    @Column(name = "img_url")
    private String imgUrl;

    private Integer hit;
}
