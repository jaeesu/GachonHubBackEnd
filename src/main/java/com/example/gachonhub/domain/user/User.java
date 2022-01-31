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
    private Long id; //id

    private String nickname;

    private String name; //login

    private String password;

    private String avatar_url; //avatar_url

    private String company;

    private String description;

    private Boolean graduate;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String created_at;

    private String githubToken;

    public enum Role {
        ADMIN, USER,
    }

    //set => list 오류 사라짐
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<UserSns> sns = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<UserRepos> repos = new HashSet<>();

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private Set<UserToTeam> groups = new HashSet<>();
}
