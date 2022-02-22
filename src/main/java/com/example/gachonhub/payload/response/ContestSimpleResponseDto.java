package com.example.gachonhub.payload.response;

import com.example.gachonhub.domain.contest.PostContest;
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
