package com.example.gachonhub.domain.user.userInfo;

import com.example.gachonhub.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@Table(name = "user_repository")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRepos {

    @Id
    @Column(name = "user_repository_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String url;

    private String title;

    private String description;

    private String lang;

    private boolean main;

    private LocalDateTime updatedAt;

    public void removeMain() {
        this.main = false;
    }

    public void addMain() {
        this.main = true;
    }
}
