package com.example.gachonhub.domain.user;


import com.example.gachonhub.domain.user.relation.UserToTeam;
import com.example.gachonhub.domain.user.userInfo.GithubRepos;
import com.example.gachonhub.domain.user.userInfo.UserSns;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Builder
@Table(name = "user")
@DynamicUpdate
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name = "user_id")
    private Long id;

    private String nickname;

    private String name; //login

    @Setter
    private String avatarUrl; //avatar_url

    @Setter
    private String company;

    @Setter
    private String description;

    @Setter
    private String major;

    @Setter
    private Boolean graduate;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String createdAt;

    @Setter
    private String githubToken;

    @Setter
    private Long commitCount;

    public enum Role {
        ADMIN, USER,
    }

    @Builder.Default
    @OneToMany(mappedBy = "userId", cascade = CascadeType.ALL) //persist를 빼니 doesn't have a default value
    private Set<UserSns> sns = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<GithubRepos> repos = new HashSet<>();

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private Set<UserToTeam> groups = new HashSet<>();

}
