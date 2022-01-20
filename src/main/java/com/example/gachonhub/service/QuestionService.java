package com.example.gachonhub.service;

import com.example.gachonhub.domain.category.SubCategory;
import com.example.gachonhub.domain.category.SubCategoryRepository;
import com.example.gachonhub.domain.file.UserFile;
import com.example.gachonhub.domain.question.PostQuestion;
import com.example.gachonhub.domain.question.QuestionRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.payload.request.QuestionRequestDto;
import com.example.gachonhub.payload.response.QuestionListResponseDto;
import com.example.gachonhub.payload.response.QuestionResponseDto;
import com.example.gachonhub.util.ImageFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final SubCategoryRepository subCategoryRepository;
    private ImageFileUtil imageFileUtil = new ImageFileUtil();

    @Transactional
    public Long saveQuestionPost(User user, QuestionRequestDto questionRequestDto) throws IOException {

        List<UserFile> userFiles = new ArrayList<>();

        if (questionRequestDto.getFiles() != null) {
            List<byte[]> bytes = imageFileUtil.convertImageToByte(questionRequestDto.getFiles());
             userFiles = bytes.stream().map(m -> UserFile.builder().image(m).build())
                    .collect(Collectors.toList());
        }//영속성 전이를 할 수 있으면서 중복을 줄일 수 있는 방법으로 코드 수정

        SubCategory subCategory = subCategoryRepository.findById(questionRequestDto.getCategory())
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 세부 카테고리입니다."));
        PostQuestion postQuestion = questionRequestDto.toEntity(user, subCategory, userFiles);
        userFiles.forEach(m -> m.updateQuestion(postQuestion));

        //strema.foreach -> foreach
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

    public Long updateQuestionPost(User user, QuestionRequestDto questionRequestDto) throws IllegalAccessException, IOException {
        PostQuestion postQuestion = findQuestionPostById(questionRequestDto.getId());
        isCorrectAuthor(user.getId(), postQuestion.getUserId().getId());

        List<UserFile> userFiles = new ArrayList<>();

        if (questionRequestDto.getFiles() != null) {
            List<byte[]> bytes = imageFileUtil.convertImageToByte(questionRequestDto.getFiles());
            userFiles = bytes.stream().map(m -> UserFile.builder().image(m).build())
                    .collect(Collectors.toList());
        }

//        Question question = questionRequestDto.update(user, files);
//        question.removeFiles();

        userFiles.forEach(m -> m.updateQuestion(postQuestion));

        return questionRepository.save(postQuestion).getId();
    }

    public PostQuestion findQuestionPostById(Long id) {
        return questionRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("해당 번호의 글이 존재하지 않습니다.")
        );
    }

    //사용자 확인
    public void isCorrectAuthor(Long userId, Long postAuthorId) throws IllegalAccessException {
        if (userId != postAuthorId) {
            throw new IllegalAccessException();
        }
    }
}
