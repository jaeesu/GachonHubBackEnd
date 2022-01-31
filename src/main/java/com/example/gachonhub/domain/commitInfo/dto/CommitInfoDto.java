package com.example.gachonhub.domain.commitInfo.dto;

import com.example.gachonhub.domain.commitInfo.CommitInfo;
import com.example.gachonhub.domain.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

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
}