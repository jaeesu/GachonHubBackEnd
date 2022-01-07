package com.example.gachonhub.controller;

import com.example.gachonhub.domain.category.SecondaryCategory;
import com.example.gachonhub.domain.file.FileRepository;
import com.example.gachonhub.domain.question.QuestionRepository;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.payload.request.QuestionRequestDto;
import com.example.gachonhub.service.QuestionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc //
@DataJpaTest
class QuestionControllerTest {

    @InjectMocks
    private QuestionController questionController;

    @Spy
    QuestionRepository questionRepository;

    @Mock
    QuestionService questionService;

    @Mock
    UserRepository userRepository;

    @Autowired
    FileRepository fileRepository;

    private MockMvc mockMvc;

    @BeforeEach
    void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(questionController).build();
    }

    @Test
    @DisplayName("질문글 작성 성공 테스트")
    @WithMockUser(roles = "USER")
    public void questionTest1() throws Exception {

        MultipartFile file = new MockMultipartFile("testImage",
                new FileInputStream((new ClassPathResource("test/testImage.jpeg")).getFile()));
        MultipartFile file2 = new MockMultipartFile("testImage2",
                new FileInputStream((new ClassPathResource("test/testImage2.jpeg")).getFile()));

        List<MultipartFile> fileList = new ArrayList<>();
        fileList.add(file);
        fileList.add(file2);


        QuestionRequestDto build = QuestionRequestDto.builder()
                .title("test title")
                .category(SecondaryCategory.TEST.name())
                .content("test content")
                .files(fileList)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();

        ResultActions result = mockMvc.perform(
                post("/api/posts/question")
                        .contentType("multipart/form-data")
                        .content(objectMapper.writeValueAsString(build)));
        result.andExpect(status().isOk());
        assertThat(questionRepository.findAll().size()).isEqualTo(1);
        assertThat(fileRepository.findAll().size()).isEqualTo(2);
    }
}