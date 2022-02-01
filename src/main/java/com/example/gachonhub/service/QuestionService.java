package com.example.gachonhub.service;

import com.example.gachonhub.domain.category.SubCategory;
import com.example.gachonhub.domain.category.SubCategoryRepository;
import com.example.gachonhub.domain.file.UserFile;
import com.example.gachonhub.domain.question.PostQuestion;
import com.example.gachonhub.domain.question.QuestionRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.request.QuestionRequestDto;
import com.example.gachonhub.payload.response.QuestionListResponseDto;
import com.example.gachonhub.payload.response.QuestionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.gachonhub.util.ErrorUtil.NOT_FOUND_CONTENT_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final UserFileService fileService;

    @Transactional
    public Long saveQuestionPost(User user, QuestionRequestDto dto) {

        List<UserFile> userFiles = fileService.uploadMultiPartToUserFile(dto.getFiles());
        SubCategory subCategory = subCategoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 세부 카테고리입니다."));

        PostQuestion postQuestion = dto.toEntity(user, subCategory, userFiles);
        userFiles.forEach(m -> m.updateQuestion(postQuestion));

        return questionRepository.save(postQuestion).getId();
    }

    public QuestionListResponseDto findAllQuestionPostsByPage(int page) {
        PageRequest request = PageRequest.of(page, 15);
        Page<PostQuestion> all = questionRepository.findAll(request);
        QuestionListResponseDto questionListResponseDto = new QuestionListResponseDto(all);
        return questionListResponseDto;
    }

    public QuestionResponseDto findQuestionPost(Long id) {
        PostQuestion postQuestion = findQuestionPostById(id);
        return new QuestionResponseDto(postQuestion);
    }

    public Long updateQuestionPost(User user, QuestionRequestDto dto) throws IllegalAccessException, IOException {
        PostQuestion question = findQuestionPostById(dto.getId());
        isCorrectAuthor(user.getId(), question.getUserId().getId());
        fileService.deleteUserFileByQuestionId(question.getId());
        return saveQuestionPost(user, dto);
    }

    public PostQuestion findQuestionPostById(Long id) {
        return questionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID)
        );
    }

    //사용자 확인
    public void isCorrectAuthor(Long userId, Long postAuthorId) throws IllegalAccessException {
        if (userId != postAuthorId) {
            throw new IllegalAccessException();
        }
    }
}
