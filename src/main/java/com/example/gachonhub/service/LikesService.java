package com.example.gachonhub.service;

import com.example.gachonhub.domain.comment.Comment;
import com.example.gachonhub.domain.comment.CommentRepository;
import com.example.gachonhub.domain.contest.PostContentRepository;
import com.example.gachonhub.domain.contest.PostContest;
import com.example.gachonhub.domain.likes.Likes;
import com.example.gachonhub.domain.likes.LikesRepository;
import com.example.gachonhub.domain.question.PostQuestion;
import com.example.gachonhub.domain.question.QuestionRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.request.LikesRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.gachonhub.domain.likes.Likes.Type.*;
import static com.example.gachonhub.util.ErrorUtil.NOT_FOUND_CONTENT_ID;

@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final CommentRepository commentRepository;
    private final QuestionRepository questionRepository;
    private final PostContentRepository contentRepository;

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
