package com.example.gachonhub.question.ui.dto;

import com.example.gachonhub.category.domain.SubCategory;
import com.example.gachonhub.comment.domain.Comment;
import com.example.gachonhub.file.domain.UserFile;
import com.example.gachonhub.likes.domain.Likes;
import com.example.gachonhub.question.domain.PostQuestion;
import com.example.gachonhub.exception.ResourceNotFoundException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
public class QuestionResponseDto {

    private Long id;
    private String user;
    private String title;
    private String content;
    private SubCategory category;
    private LocalDate writeAt;
    private Long hit;

    private List<FileResponseDto> fileList;
    private List<CommentResponseDto> commentList;

    public QuestionResponseDto(PostQuestion postQuestion) {
        this.id = postQuestion.getId();
        this.user = postQuestion.getUserId().getNickname();
        this.title = postQuestion.getTitle();
        this.content = postQuestion.getContent();
        this.category = postQuestion.getCategoryId();
        this.writeAt = postQuestion.getCreatedAt().toLocalDate();
        this.hit = postQuestion.getHit();
        this.fileList = postQuestion.getUserFileList().stream()
                .map(FileResponseDto::new).collect(Collectors.toList());
        this.commentList = postQuestion.getCommentList().stream()
                .map(CommentResponseDto::new).collect(Collectors.toList());
    }

    @Getter
    @Setter
    public static class FileResponseDto {
        private Long id;
        private Long questionId;
        private String image;

        public FileResponseDto(UserFile userFile) {
            this.id = userFile.getId();
            this.questionId = userFile.getPostQuestionId().getId();
            this.image = userFile.getImageUrl();
        }
    }

    @Getter
    @Setter
    public static class CommentResponseDto {

        //댓글(댓글 작성자, 시각, 내용, 좋아요 수), 댓글 수

        private Long id;
        private String userId;
        private String content;
        private Long questionId;
        private long superCommnetId;
        private LocalDate writeAt;
        private List<LikesResponseDto> likesList;

        public CommentResponseDto toDto(Optional<Comment> comment) {
            comment.map(CommentResponseDto::new).orElseThrow(() -> new ResourceNotFoundException());
            return this;
        }

        public CommentResponseDto(Comment comment) {
            this.id = comment.getId();
            this.userId = comment.getUserId().getNickname();
            this.content = comment.getContent();
            this.questionId = comment.getPostQuestionId().getId();
            this.superCommnetId = (Optional.ofNullable(comment.getParentComment()).isPresent()) ? comment.getParentComment().getId() : 0;
            this.writeAt = comment.getCreatedAt().toLocalDate();
            this.likesList = comment.getLikesList().stream()
                    .map(LikesResponseDto::new).collect(Collectors.toList());
        }
    }

    @Getter
    @Setter
    public static class LikesResponseDto {

        private Long id;
        private String userId;
        private Long commentId;
        private Long questionId;
        private Long contestId;

        public List<LikesResponseDto> toDto(List<Likes> list) {
            return list.stream()
                    .map(LikesResponseDto::new)
                    .collect(Collectors.toList());
        }

        public LikesResponseDto(Likes likes) {
            this.id = likes.getId();
            this.userId = likes.getUser().getNickname();
            this.commentId = likes.getParentComment().getId();
            this.questionId = likes.getPostQuestionId().getId();
            this.contestId = likes.getPostContest().getId();
        }
    }

}

