package com.example.gachonhub.payload.response;

import com.example.gachonhub.domain.question.Question;
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

    public QuestionListResponseDto(Page<Question> questions) {
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
        private Timestamp writeAt;

        public ListQuestionDto(Question question) {
            this.id = question.getId();
            this.title = question.getTitle();
            this.user = question.getUserId().getNickname();
            this.category = question.getCategory();
            this.hit = question.getHit();
            this.likes = (long) question.getLikesList().size();
            this.writeAt = question.getWriteAt();

        }
    }


}
