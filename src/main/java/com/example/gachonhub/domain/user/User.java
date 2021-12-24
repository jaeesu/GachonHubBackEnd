package com.example.gachonhub.domain.user;


import com.example.gachonhub.domain.user.relation.UserToGroup;
import com.example.gachonhub.domain.user.userInfo.UserRepos;
import com.example.gachonhub.domain.user.userInfo.UserSns;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
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
    private Long id;

    private String name;

    private String major;

    //
    private String email;

    private String password;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "commit_count")
    private Long commitCount;

    private Role role;

    @Column(name = "is_graduate")
    private boolean graduate;

    private String description;

    private String token;

    public enum Role {
        ADMIN, USER
    }
}
