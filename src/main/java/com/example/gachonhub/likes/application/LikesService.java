package com.example.gachonhub.likes.application;

import com.example.gachonhub.comment.domain.Comment;
import com.example.gachonhub.comment.domain.CommentRepository;
import com.example.gachonhub.contest.domain.PostContestRepository;
import com.example.gachonhub.contest.domain.PostContest;
import com.example.gachonhub.likes.domain.Likes;
import com.example.gachonhub.likes.domain.LikesRepository;
import com.example.gachonhub.question.domain.PostQuestion;
import com.example.gachonhub.question.domain.QuestionRepository;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.likes.ui.dto.LikesRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.gachonhub.likes.domain.Likes.Type.*;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_CONTENT_ID;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;
    private final PostContestRepository contentRepository;

    public void saveLikes(User user, LikesRequestDto dto) {
        Likes likes = Likes.builder().build();
        if (dto.getType().equals(QUESTION)) {
            PostQuestion questionById = findQuestionById(dto.getId());
            likes = dto.toEntity(user, questionById);
        } else if (dto.getType().equals(QUESTION_COMMENT)) {
            Comment commentById = findCommentById(dto.getId());
            likes = dto.toEntity(user, commentById);
        } else if (dto.getType().equals(CONTEST)) {
            PostContest contentById = findContestById(dto.getId());
            likes = dto.toEntity(user, contentById);
        }
        likesRepository.save(likes);
    }

    public void deleteLikes(User user, LikesRequestDto dto) {
        if (dto.getType().equals(QUESTION)) {
            likesRepository.deleteByUser_IdAndPostQuestionId_Id(user.getId(), dto.getId());
        } else if (dto.getType().equals(QUESTION_COMMENT)) {
            likesRepository.deleteByUser_IdAndParentComment_Id(user.getId(), dto.getId());
        } else if (dto.getType().equals(CONTEST)) {
            likesRepository.deleteByUser_IdAndPostContest_Id(user.getId(), dto.getId());
        }
    }

    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID));
    }

    public PostQuestion findQuestionById(Long id) {
        return questionRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID));
    }

    public PostContest findContestById(Long id) {
        return contentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID));
    }

}
