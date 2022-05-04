package com.example.gachonhub.inquiry.domain;

import com.example.gachonhub.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Builder
@Table(name = "post_inquiry")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostInquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_inquiry_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private String title;

    private String content;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "write_at")
    @Builder.Default
    private Timestamp writeAt = new Timestamp(System.currentTimeMillis());

    @Column(name = "password")
    private Integer password;

    @Column(name = "secret")
    private boolean secret;
}
