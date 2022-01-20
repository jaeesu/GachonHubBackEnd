package com.example.gachonhub.domain.user.userInfo;

import com.example.gachonhub.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "user_repository")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRepos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_repository_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String url;

    private String title;

    private String description;

    private String lang;
}
