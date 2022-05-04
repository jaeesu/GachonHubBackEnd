package com.example.gachonhub.comment.application;

import com.example.gachonhub.comment.domain.Comment;
import com.example.gachonhub.comment.domain.CommentRepository;
import com.example.gachonhub.question.domain.PostQuestion;
import com.example.gachonhub.question.domain.QuestionRepository;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.exception.NotAccessUserException;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.common.ui.in.CommentRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.gachonhub.common.exception.ErrorUtil.NOT_CORRECT_USER_ID;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_CONTENT_ID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;

    public void saveComment(User user, CommentRequestDto dto) {
        PostQuestion questionById = findQuestionById(dto.getQuestionId());
        Comment commentById = (dto.getParentCommentId() != null) ? findCommentById(dto.getParentCommentId()) : null;
        Comment comment = dto.toEntity(user, questionById, commentById);
        commentRepository.save(comment);
    }

    public void updateComment(User user, CommentRequestDto dto) {
        PostQuestion questionById = findQuestionById(dto.getQuestionId());
        Comment commentById = findCommentById(dto.getId());
        isCorrectAuthor(user.getId(), commentById.getUserId().getId());
        Comment parent = (dto.getParentCommentId() != null) ? findCommentById(dto.getParentCommentId()) : null;
        Comment comment = dto.toEntity(user, questionById, parent);
        commentRepository.save(comment);
    }

    public void deleteComment(User user, Long id)  {
        Comment commentById = findCommentById(id);
        isCorrectAuthor(user.getId(), commentById.getUserId().getId());
        commentRepository.deleteById(id);
    }


    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID)
        );
    }

    public PostQuestion findQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID)
        );
    }

    public void isCorrectAuthor(Long userId, Long commentAuthorId)  {
        if (!userId.equals(commentAuthorId)) {
            throw new NotAccessUserException(NOT_CORRECT_USER_ID);
        }
    }
}
