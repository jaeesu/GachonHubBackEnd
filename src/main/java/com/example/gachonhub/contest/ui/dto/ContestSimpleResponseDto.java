package com.example.gachonhub.contest.ui.dto;

import com.example.gachonhub.contest.domain.PostContest;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ContestSimpleResponseDto {

    private Long id;
    private String mainImage;

    public static ContestSimpleResponseDto fromEntity(PostContest contest) {
        return ContestSimpleResponseDto.builder()
                .id(contest.getId())
                .mainImage(contest.getImage())
                .build();
    }
}
