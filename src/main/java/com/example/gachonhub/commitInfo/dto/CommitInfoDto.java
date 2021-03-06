package com.example.gachonhub.commitInfo.dto;

import com.example.gachonhub.commitInfo.domain.CommitInfo;
import com.example.gachonhub.team.domain.Team;
import com.example.gachonhub.user.domain.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ToString
@Setter
public class CommitInfoDto {

    private String sha;
    private Commit commit;

    @Getter
    @ToString
    @Setter
    @NoArgsConstructor
    public static  class Commit {
        private String message;
        private Author author;

        @Getter
        @ToString
        @Setter
        @NoArgsConstructor
        public static class Author {
            private String name;
            private String email;
            private LocalDateTime date;
        }
    }

    public CommitInfo toEntity(User user) {
        return CommitInfo.builder()
                .sha(this.sha)
                .message(this.commit.message)
                .date(this.commit.author.date)
                .userId(user)
                .build();

    }

    public CommitInfo toEntity(Team team) {
        return CommitInfo.builder()
                .sha(this.sha)
                .message(this.commit.message)
                .date(this.commit.author.date)
                .teamId(team)
                .build();

    }
}
