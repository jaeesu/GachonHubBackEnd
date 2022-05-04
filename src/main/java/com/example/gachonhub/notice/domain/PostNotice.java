package com.example.gachonhub.notice.domain;

import com.example.gachonhub.post.domain.Post;
import com.example.gachonhub.user.domain.User;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Table(name = "post_notice")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PostNotice extends Post {

    @Builder
    public PostNotice(Long id, User userId, String title, String content) {
        super(id, userId, title, content);
    }
}
