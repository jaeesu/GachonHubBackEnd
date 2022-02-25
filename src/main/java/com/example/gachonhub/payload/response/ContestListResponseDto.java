package com.example.gachonhub.payload.response;

import com.example.gachonhub.domain.contest.PostContest;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
public class ContestListResponseDto {

    private int totalPages;
    private int page;
    private List<ListContestDto> data;

    public ContestListResponseDto(Page<PostContest> contests) {
        this.totalPages = contests.getTotalPages();
        this.page = contests.getNumber();
        this.data = contests.getContent().stream()
                .map(ListContestDto::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class ListContestDto {
        private Long id;
        private String authorId;
        private String title;
        private String content;
        private String category;
        private String mainImage;

        public ListContestDto(PostContest contest) {
            this.id = contest.getId();
            this.authorId = contest.getUser().getNickname();
            this.title = contest.getTitle();
            this.content = contest.getContent();
            this.category = contest.getCategoryId().getName();
            this.mainImage = contest.getImage();
        }
    }

}
