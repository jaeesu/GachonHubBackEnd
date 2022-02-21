package com.example.gachonhub.domain.user.userInfo;

import com.example.gachonhub.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@Table(name = "personal_sns")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_sns_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId; //user_id_user_id

    private String category;

    private String url;

    @Getter
    public enum SnsCategory {
        TISTORY("tistory"),
        GITHUB("github.com"),
        GITHUB_BLOG("github.blog"),
        VELOG("velog"),
        BRUNCH("brunch"),
        FACEBOOK("facebook"),
        INSTAGRAM("instagram"),
        TWITTER("twitter");

        private String title;

        SnsCategory(String title) {
            this.title = title;
        }
    }
}
