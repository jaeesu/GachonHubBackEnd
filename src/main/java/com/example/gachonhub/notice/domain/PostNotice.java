package com.example.gachonhub.notice.domain;

import com.example.gachonhub.common.domain.BaseTimeEntity;
import com.example.gachonhub.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Builder
@Table(name = "post_notice")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostNotice extends BaseTimeEntity {

    //조회수??

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_notice_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    private String title;

    private String content;
}
