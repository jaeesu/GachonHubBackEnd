package com.example.gachonhub.domain.user.userInfo;

import com.example.gachonhub.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "user_sns")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSns {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_sns_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String category;

    private String url;
}
