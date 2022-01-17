package com.example.gachonhub.service;

import com.example.gachonhub.domain.file.File;
import com.example.gachonhub.domain.question.Question;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ImageFileUtil imageFileUtil;

    @Transactional
    public Long saveQuestionPost(User user, QuestionRequestDto questionRequestDto) throws IOException {

        List<File> files = new ArrayList<>();

        if (questionRequestDto.getFiles() != null) {
            List<byte[]> bytes = imageFileUtil.convertImageToByte(questionRequestDto.getFiles());
             files = bytes.stream().map(m -> File.builder().image(m).build())
                    .collect(Collectors.toList());
        }//영속성 전이를 할 수 있으면서 중복을 줄일 수 있는 방법으로 코드 수정

        Question question = questionRequestDto.toEntity(user, files);
        files.forEach(m -> m.setQuestionId(question));

        //strema.foreach -> foreach
        return questionRepository.save(question).getId();
    }

    public QuestionListResponseDto findAllQuestionPostsByPage(int page) {
//        PageRequest request = PageRequest.of(page, 15, Sort.Direction.DESC);
        PageRequest request = PageRequest.of(page, 15);
        Page<Question> all = questionRepository.findAll(request);
        QuestionListResponseDto questionListResponseDto = new QuestionListResponseDto(all);
        return questionListResponseDto;
    }

    public QuestionResponseDto findQuestionPost(Long id) {
        Question question = findQuestionPostById(id);
        return new QuestionResponseDto(question);
    }

    public Long updateQuestionPost(User user, QuestionRequestDto questionRequestDto) throws IllegalAccessException, IOException {
        Question question = findQuestionPostById(questionRequestDto.getId());
        isCorrectAuthor(user.getId(), question.getUserId().getId());

        List<File> files = new ArrayList<>();

        if (questionRequestDto.getFiles() != null) {
            List<byte[]> bytes = imageFileUtil.convertImageToByte(questionRequestDto.getFiles());
            files = bytes.stream().map(m -> File.builder().image(m).build())
                    .collect(Collectors.toList());
        }

        Question question1 = questionRequestDto.toEntity(user, files);
        files.forEach(m -> m.setQuestionId(question1));

        return questionRepository.save(question1).getId();

    }

    public Question findQuestionPostById(Long id) {
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
