package com.example.gachonhub.domain.notice;

import com.example.gachonhub.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Builder
@Table(name = "post_notice")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostNotice {

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

    @Column(name = "write_at")
    @Builder.Default
    private Timestamp writeAt = new Timestamp(System.currentTimeMillis());;
}
