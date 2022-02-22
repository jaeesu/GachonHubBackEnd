package com.example.gachonhub.payload.response;


import com.example.gachonhub.domain.category.SubCategory;
import com.example.gachonhub.domain.contest.PostContest;
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
    private SubCategory categoryId;
    private Integer hit;
    private LocalDate writeAt;
    private String image;

    public static ContestResponseDto fromContest(PostContest contest) {
        return ContestResponseDto.builder()
                .id(contest.getId())
                .user(contest.getUser().getNickname())
                .title(contest.getTitle())
                .content(contest.getContent())
                .categoryId(contest.getCategoryId())
                .hit(contest.getHit())
                .writeAt(contest.getWriteAt().toLocalDateTime().toLocalDate())
                .image(contest.getImage())
                .build();
    }

}
