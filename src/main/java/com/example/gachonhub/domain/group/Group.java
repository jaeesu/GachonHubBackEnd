package com.example.gachonhub.domain.group;

import com.example.gachonhub.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_group")
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String repos;

    @Enumerated(EnumType.STRING)
    private GroupType type;

    private String description;

    private String name;

    private String commitCount;

    @Column(name = "is_recruit")
    private boolean recruiting;

    @Column(name = "recruit_content")
    private String recruitContent;

    public enum GroupType {
        STUDY, CREW
    }
}
