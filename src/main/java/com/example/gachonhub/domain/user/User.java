package com.example.gachonhub.domain.user;


import com.example.gachonhub.domain.user.relation.UserToGroup;
import com.example.gachonhub.domain.user.userInfo.UserRepos;
import com.example.gachonhub.domain.user.userInfo.UserSns;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter
@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_id")
    private Long id; //id

    private String nickname;

    private String name; //login

    private String password;

    private String avatar_url; //avatar_url

    private String home_url; //html_url

    private String user_name;

    private String company;

    private String blog;

    private String email;

    private String repo1;
    private String repo2;
    private String repo3;

    private String description;

    private String graduate;

    private String token;

    private String created_at;

    private String updated_at;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        ADMIN, USER,
    }
}
