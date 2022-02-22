package com.example.gachonhub.payload.response;

import com.example.gachonhub.domain.contest.PostContest;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ContestListResponseDto {

    private int totalPages;
    private int page;
    private List<ListContestDto> data;

    public static ContestListResponseDto fromPagable(Page<PostContest> contests) {
        return ContestListResponseDto.builder()
                .totalPages(contests.getTotalPages())
                .page(contests.getNumber())
                .data(contests.stream()
                        .map(ListContestDto::new)
                        .collect(Collectors.toList()))
                .build();
    }

    @Getter
    static class ListContestDto {
        private String title;
        private String content;
        private String authorId;
        private String mainImage;
        private String category;

        ListContestDto(PostContest contest) {
            this.title = contest.getTitle();
            this.content = contest.getContent();
            this.authorId = contest.getUser().getNickname();
            this.mainImage = contest.getImage();
            this.category = contest.getCategoryId().getName();
        }
    }
}
