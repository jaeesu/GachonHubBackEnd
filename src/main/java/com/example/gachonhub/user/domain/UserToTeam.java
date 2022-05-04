package com.example.gachonhub.user.domain;

import com.example.gachonhub.team.domain.Team;
import com.example.gachonhub.user.domain.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Setter
@ToString
@Table(name = "user_team")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserToTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_team_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
}
