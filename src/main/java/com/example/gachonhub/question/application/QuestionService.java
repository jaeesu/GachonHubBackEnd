package com.example.gachonhub.question.application;

import com.example.gachonhub.category.domain.SubCategory;
import com.example.gachonhub.category.domain.SubCategoryRepository;
import com.example.gachonhub.file.domain.UserFile;
import com.example.gachonhub.file.application.UserFileService;
import com.example.gachonhub.question.domain.PostQuestion;
import com.example.gachonhub.question.domain.QuestionRepository;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.exception.NotAccessUserException;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.question.ui.dto.QuestionRequestDto;
import com.example.gachonhub.question.ui.dto.QuestionListResponseDto;
import com.example.gachonhub.question.ui.dto.QuestionResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.gachonhub.common.exception.ErrorUtil.NOT_CORRECT_USER_ID;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_CONTENT_ID;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final UserFileService fileService;

    @Transactional
    public Long saveQuestionPost(User user, QuestionRequestDto dto) {

        List<UserFile> userFiles = null;

        if (dto.getFiles() != null) {
            userFiles = fileService.uploadMultiPartToUserFile(dto.getFiles());
        }
        SubCategory subCategory = subCategoryRepository.findById(dto.getCategory())
                .orElseThrow(() -> new ResourceNotFoundException("존재하지 않는 세부 카테고리입니다."));

        PostQuestion postQuestion = dto.toEntity(user, subCategory, userFiles);

        if (dto.getFiles() != null) {
            userFiles.forEach(m -> m.updateQuestion(postQuestion));
        }

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

    public Long updateQuestionPost(User user, QuestionRequestDto dto) {
        PostQuestion question = findQuestionPostById(dto.getId());
        isCorrectAuthor(user.getId(), question.getUser().getId());
//        fileService.deleteUserFileByQuestionId(question.getId());
        return saveQuestionPost(user, dto);
    }

    public PostQuestion findQuestionPostById(Long id) {
        return questionRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(NOT_FOUND_CONTENT_ID)
        );
    }

    //사용자 확인
    public void isCorrectAuthor(Long userId, Long postAuthorId) {
        if (!userId.equals(postAuthorId)) {
            throw new NotAccessUserException(NOT_CORRECT_USER_ID);
        }
    }
}
