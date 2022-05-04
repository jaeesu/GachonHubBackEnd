package com.example.gachonhub.question.ui;

import com.example.gachonhub.common.config.SecurityConfig;
import com.example.gachonhub.question.ui.QuestionController;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.question.application.QuestionService;
import com.example.gachonhub.support.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@WebMvcTest(controllers = QuestionController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("질문글 api 테스트")
class PostQuestionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QuestionService questionService;

    @MockBean
    private UserRepository userRepository;

    @Nested
    @DisplayName("질문글 작성 테스트")
    class creation {
        @Test
        @DisplayName("성공")
//        @WithMockUser(roles = "USER")
        @WithMockCustomUser //user details가 null이어서 테스트를 통과하지 못할 경우 => details를 넣은 커스텀 애노테이션
        public void questionTest1() throws Exception {

            MockMultipartFile file = new MockMultipartFile("testImage",
                    new FileInputStream((new ClassPathResource("test/testImage.jpeg")).getFile()));
            MockMultipartFile file2 = new MockMultipartFile("testImage2",
                    new FileInputStream((new ClassPathResource("test/testImage2.jpeg")).getFile()));

            List<MultipartFile> fileList = new ArrayList<>();
            fileList.add(file);
            fileList.add(file2);

            User user = User.builder().id(1234L).nickname("test").name("test").role(User.Role.USER).build();

            given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));
            given(questionService.saveQuestionPost(any(), any())).willReturn(1L);

            mockMvc.perform(
                    multipart("/api/posts/question")
                            .file(file)
                            .file(file2)
                            .param("title", "test title")
                            .param("category", "2")
                            .param("content", "test contest")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                    //csrf가 없을 때 테스트 실패 => 단 spring boot test, autoconfiguremvctest? 애노테이션을 달았을 때 성고
                    //csrf를 넣었을 때 테스트 성공
            ).andExpect(status().isOk());

        }

        @Test
        @DisplayName("성공 (파일 누락)")
//        @WithMockUser(roles = "USER")
        @WithMockCustomUser
            //기존에 withmockuser, withmockcustomuser 둘다 성공하는 것을 확인했는데 갑자기 withmockuser가 안돌아간다.
            //withmockuser를 달지 않았을 때 github 인증 서버로의 리다이렉트 실패로 302에러가 발생한다.
        void questionTest2() throws Exception {
            User user = User.builder().id(1234L).nickname("test").name("test").role(User.Role.USER).build();

            given(userRepository.findById(any())).willReturn(Optional.ofNullable(user));
            given(questionService.saveQuestionPost(any(), any())).willReturn(1L);

            mockMvc.perform(
                    multipart("/api/posts/question")
                            .param("title", "test title")
                            .param("category", "2")
                            .param("content", "test contest")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            ).andExpect(status().isOk());

        }

        @Test
        @DisplayName("실패 (제목 누락)")
        @WithMockUser(roles = "USER")
        void questionTest3() throws Exception {

            mockMvc.perform(
                            multipart("/api/posts/question")
                                    .param("category", "2")
                                    .param("content", "test contest")
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("제목이 누락되었습니다."));
        }

        @Test
        @DisplayName("실패 (카테고리 누락)")
        @WithMockUser(roles = "USER")
        void questionTest4() throws Exception {

            mockMvc.perform(
                            multipart("/api/posts/question")
                                    .param("title", "test title")
                                    .param("content", "test contest")
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("카테고리가 누락되었습니다."));
        }

        @Test
        @DisplayName("실패 (내용 누락)")
        @WithMockUser(roles = "USER")
        void questionTest5() throws Exception {

            mockMvc.perform(
                            multipart("/api/posts/question")
                                    .param("title", "test title")
                                    .param("category", "2")
                                    .with(SecurityMockMvcRequestPostProcessors.csrf())
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("내용이 누락되었습니다."));
        }
    }

//    @Nested
//    @DisplayName("질문글 수정 테스트")
//    class list {
//
//        @Test
//        @DisplayName("성공")
//        @WithMockCustomUser
//        void questionTest6(){
//            // /api/posts/question put
//            //given
//
//
//            //when
//
//            //then
//        }
//    }


}