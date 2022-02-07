package com.example.gachonhub.domain.user;


import com.example.gachonhub.domain.user.relation.UserToTeam;
import com.example.gachonhub.domain.user.userInfo.UserRepos;
import com.example.gachonhub.domain.user.userInfo.UserSns;
import lombok.*;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Builder
@Table(name = "user")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_id")
    private Long id;

    private String nickname;

    private String name; //login

    private String avatarUrl; //avatar_url

    private String company;

    private String description;

    private String major;

    private Boolean graduate;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String createdAt;

    private String githubToken;

    public enum Role {
        ADMIN, USER,
    }

    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL) //persist를 빼니 doesn't have a default value
    private Set<UserSns> sns = new HashSet<>();

    @Builder.Default
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<UserRepos> repos = new HashSet<>();

    @Builder.Default
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<UserToTeam> groups = new HashSet<>();
}
