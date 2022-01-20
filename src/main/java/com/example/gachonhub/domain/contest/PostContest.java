package com.example.gachonhub.domain.contest;

import com.example.gachonhub.domain.category.SubCategory;
import com.example.gachonhub.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Date;
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

    private String title;

    private String content;

    @ManyToOne
    private SubCategory categoryId;

    private Integer people;

    private Date start;

    private Date end;

    private Integer hit;

    private Timestamp writeAt;

    @Lob
    private byte[] image;

}
