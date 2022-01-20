package com.example.gachonhub.controller;

import com.example.gachonhub.config.SecurityConfig;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.payload.request.NoticeRequestDto;
import com.example.gachonhub.service.NoticeService;
import com.example.gachonhub.support.WithMockCustomUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static com.example.gachonhub.domain.user.User.Role.USER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@WebMvcTest(controllers = NoticeController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("공지사항 api 테스트")
class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private NoticeService noticeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("공지사항 작성 테스트")
    @WithMockCustomUser
    class NoticeSaveTest {

        @Test
        @DisplayName("성공")
        void saveSuccessTest() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            NoticeRequestDto dto = NoticeRequestDto.builder()
                    .title("title")
                    .content("content")
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/posts/notice")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk());
        }

        @Test
        @DisplayName("실패 (제목 누락)")
        void saveFileTest1() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            NoticeRequestDto dto = NoticeRequestDto.builder()
                    .content("content")
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/posts/notice")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("제목이 누락되었습니다."));
        }

        @Test
        @DisplayName("실패 (내용 누락)")
        void saveFileTest2() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            NoticeRequestDto dto = NoticeRequestDto.builder()
                    .title("title")
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/posts/notice")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("내용이 누락되었습니다."));
        }

    }

    @Nested
    @WithMockCustomUser
    @DisplayName("공지사항 수정 테스트")
    class UpdateNoticeTest {

        @Test
        @DisplayName("성공")
        void updateSuccessTest() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            NoticeRequestDto dto = NoticeRequestDto.builder()
                    .id(1L)
                    .title("title")
                    .content("content")
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/posts/notice")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk());
        }

        @Test
        @DisplayName("실패 (아이디 누락)")
        void updateNoticeTest1() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            NoticeRequestDto dto = NoticeRequestDto.builder()
                    .title("title1")
                    .content("content")
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/posts/notice")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("아이디가 누락되었습니다."));
        }

        @Test
        @DisplayName("실패 (제목 누락)")
        void updateNoticeTest2() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            NoticeRequestDto dto = NoticeRequestDto.builder()
                    .id(1L)
                    .content("content")
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/posts/notice")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("제목이 누락되었습니다."));
        }

        @Test
        @DisplayName("실패 (내용 누락)")
        void updateNoticeTest3() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            NoticeRequestDto dto = NoticeRequestDto.builder()
                    .id(1L)
                    .title("title")
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/posts/notice")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("내용이 누락되었습니다."));
        }
    }

    @Nested
    @DisplayName("공지사항 삭제 테스트")
    @WithMockCustomUser
    class DeleteNoticeTest {

        @Test
        @DisplayName("성공")
        void deleteSuccessTest() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    delete("/api/posts/notice/1")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("공지사항 삭제 완료"));
        }

    }

    User getTestUser() {
        User build = User.builder()
                .id(1234L)
                .nickname("test")
                .role(USER)
                .build();
        return build;
    }
}