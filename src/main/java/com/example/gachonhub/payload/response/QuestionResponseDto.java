package com.example.gachonhub.payload.response;

import com.example.gachonhub.domain.comment.Comment;
import com.example.gachonhub.domain.file.File;
import com.example.gachonhub.domain.likes.Likes;
import com.example.gachonhub.domain.question.Question;
import org.springframework.beans.BeanUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

public class QuestionResponseDto {

    private Long id;
    private String user;
    private String title;
    private String content;
    private String category;
    private LocalDate writeAt;
    private Long hit;

    private List<File> fileList = new ArrayList<>();
    private List<CommentResponseDto> commentList = new ArrayList<>();

    public QuestionResponseDto(Question question) {
        this.id = question.getId();
        this.user = question.getUserId().getNickname();
        this.title = question.getTitle();
        this.content = question.getContent();
        this.category = question.getCategory();
        this.writeAt = question.getWriteAt().toLocalDateTime().toLocalDate();
        this.hit = question.getHit();
        this.fileList = question.getFileList();
        this.commentList = question.getCommentList().stream()
                .map(CommentResponseDto::new).collect(Collectors.toList());
    }

    class CommentResponseDto {

        //댓글(댓글 작성자, 시각, 내용, 좋아요 수), 댓글 수

        private Long id;
        private String userId;
        private Long questionId;
        private Long superCommnetId;
        private LocalDate writeAt;
        private List<LikesResponseDto> likesList;

        public CommentResponseDto toDto(Optional<Comment> comment) {
            comment.map(CommentResponseDto::new).orElseThrow(() -> new NoSuchElementException());
            return this;
        }

        public CommentResponseDto(Comment comment) {
            this.id = comment.getId();
            this.userId = comment.getUserId().getNickname();
            this.questionId = comment.getQuestionId().getId();
            this.superCommnetId = comment.getParentComment().getId();
            this.writeAt = comment.getWriteAt().toLocalDateTime().toLocalDate();
            this.likesList = comment.getLikesList().stream()
                    .map(LikesResponseDto::new).collect(Collectors.toList());
        }
    }

    class LikesResponseDto {

        private Long id;
        private String userId;
        private Long commentId;
        private Long questionId;

        public List<LikesResponseDto> toDto(List<Likes> list) {
            return list.stream()
                    .map(LikesResponseDto::new)
                    .collect(Collectors.toList());
        }

        public LikesResponseDto(Likes likes) {
            BeanUtils.copyProperties(likes, this);
        }
    }

}

