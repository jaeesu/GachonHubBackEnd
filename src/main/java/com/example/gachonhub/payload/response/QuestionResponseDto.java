package com.example.gachonhub.payload.response;

import com.example.gachonhub.domain.category.SubCategory;
import com.example.gachonhub.domain.comment.Comment;
import com.example.gachonhub.domain.file.UserFile;
import com.example.gachonhub.domain.likes.Likes;
import com.example.gachonhub.domain.question.PostQuestion;
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
        this.writeAt = postQuestion.getWriteAt().toLocalDateTime().toLocalDate();
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
        private byte[] image;

        public FileResponseDto(UserFile userFile) {
            this.id = userFile.getId();
            this.questionId = userFile.getPostQuestionId().getId();
            this.image = userFile.getImage();
        }
    }

    @Getter
    @Setter
    public static class CommentResponseDto {

        //댓글(댓글 작성자, 시각, 내용, 좋아요 수), 댓글 수

        private Long id;
        private String userId;
        private Long questionId;
        private Long superCommnetId;
        private LocalDate writeAt;
        private List<LikesResponseDto> likesList;

        public CommentResponseDto toDto(Optional<Comment> comment) {
            comment.map(CommentResponseDto::new).orElseThrow(() -> new ResourceNotFoundException());
            return this;
        }

        public CommentResponseDto(Comment comment) {
            this.id = comment.getId();
            this.userId = comment.getUserId().getNickname();
            this.questionId = comment.getPostQuestionId().getId();
            this.superCommnetId = comment.getParentComment().getId();
            this.writeAt = comment.getWriteAt().toLocalDateTime().toLocalDate();
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
            this.questionId = likes.getPostQuestion().getId();
            this.contestId = likes.getPostContest().getId();
        }
    }

}

