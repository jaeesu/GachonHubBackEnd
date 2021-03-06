package com.example.gachonhub.contest.ui.dto;

import com.example.gachonhub.contest.domain.PostContest;
import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Getter
@Builder(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ContestResponseDto {

    private Long id;
    private String user;
    private String title;
    private String content;
    private String category;
    private String image;
    private Integer hit;
    private LocalDate writeAt;

    public static ContestResponseDto fromEntity(PostContest contest) {
        return ContestResponseDto.builder()
                .id(contest.getId())
                .user(contest.getUser().getNickname())
                .title(contest.getTitle())
                .content(contest.getContent())
                .category(contest.getCategoryId().getName())
                .image(contest.getImage())
                .hit(contest.getHit())
                .writeAt(contest.getCreatedAt().toLocalDate())
                .build();

    }
}
