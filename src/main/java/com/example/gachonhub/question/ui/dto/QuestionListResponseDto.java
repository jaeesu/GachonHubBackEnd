package com.example.gachonhub.question.ui.dto;

import com.example.gachonhub.question.domain.PostQuestion;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class QuestionListResponseDto {

    private int totalPages;
    private int page;
    private List<ListQuestionDto> data;

    public QuestionListResponseDto(Page<PostQuestion> questions) {
        this.totalPages = questions.getTotalPages();
        this.page = questions.getNumber();
        this.data = questions.getContent().stream()
                .map(ListQuestionDto::new).collect(Collectors.toList());
    }

    @Getter //getter를 달지 않아서 컨트롤러 응답이 제대로 안났다. setter도 아니고 getter인데 왜일까..
    public static class ListQuestionDto {
        private Long id;
        private String title;
        private String user;
        private String category;
        private Long hit;
        private Long likes;
        private LocalDateTime writeAt;

        public ListQuestionDto(PostQuestion postQuestion) {
            this.id = postQuestion.getId();
            this.title = postQuestion.getTitle();
            this.user = postQuestion.getUserId().getNickname();
            this.category = postQuestion.getCategoryId().getName();
            this.hit = postQuestion.getHit();
            this.likes = (long) postQuestion.getLikesList().size();
            this.writeAt = postQuestion.getCreatedAt();

        }
    }


}
