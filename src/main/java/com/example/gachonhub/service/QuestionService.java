package com.example.gachonhub.service;

import com.example.gachonhub.domain.file.File;
import com.example.gachonhub.domain.question.Question;
import com.example.gachonhub.domain.question.QuestionRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.payload.request.QuestionRequestDto;
import com.example.gachonhub.util.ImageFileUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final FileService fileService;
    private final ImageFileUtil imageFileUtil;

    @Transactional
    public Long saveQuestionPost(User user, QuestionRequestDto questionRequestDto) throws IOException {
        List<File> files = fileService.convertBytestToFile(imageFileUtil.convertImageToByte(questionRequestDto.getFiles()));
        Question question = questionRequestDto.toEntity(user, files);
        files.stream().forEach(m -> m.setQuestionId(question));
        return questionRepository.save(question).getId();
    }

    public void findAllQuestionPostsByPage(int page) {
        PageRequest request = PageRequest.of(page, 15, Sort.Direction.DESC);
        questionRepository.findAll(request);
    }

    public void findQUestionById(Long id) {
        questionRepository.findById(id);
    }
}
