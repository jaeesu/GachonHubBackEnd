package com.example.gachonhub.inquiry.domain;

import com.example.gachonhub.post.domain.Post;
import com.example.gachonhub.user.domain.User;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "post_inquiry")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostInquiry extends Post {

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "password")
    private Integer password;

    @Column(name = "secret")
    private boolean secret;

    @Builder
    public PostInquiry(Long id, User userId, String title, String content, String imgUrl, Integer password, boolean secret) {
        super(id, userId, title, content);
        this.imgUrl = imgUrl;
        this.password = password;
        this.secret = secret;
    }
}
