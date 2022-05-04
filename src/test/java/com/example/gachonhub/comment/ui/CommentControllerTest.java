package com.example.gachonhub.comment.ui;

import com.example.gachonhub.comment.ui.CommentController;
import com.example.gachonhub.common.config.SecurityConfig;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.common.ui.in.CommentRequestDto;
import com.example.gachonhub.comment.application.CommentService;
import com.example.gachonhub.support.WithMockCustomUser;
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

import static com.example.gachonhub.user.domain.User.Role.USER;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_CONTENT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@WebMvcTest(controllers = CommentController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("댓글 api 테스트")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @MockBean
    private UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("댓글 작성 테스트")
    @WithMockCustomUser
    class saveCommentTest {

        @Test
        @DisplayName("성공")
        void saveSuccessTest1() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            CommentRequestDto dto = CommentRequestDto.builder()
                    .content("content")
                    .questionId(123L)
                    .parentCommentId(111L)
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/comments")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("댓글 작성 완료"));
        }

        @Test
        @DisplayName("성공 (상위 댓글 번호 누락)")
        void saveSuccessTest2() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            CommentRequestDto dto = CommentRequestDto.builder()
                    .content("content")
                    .questionId(123L)
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/comments")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("댓글 작성 완료"));
        }

        @Test
        @DisplayName("실패 (아이디 포함)")
        void saveFailTest1() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            CommentRequestDto dto = CommentRequestDto.builder()
                    .id(1L)
                    .content("content")
                    .questionId(123L)
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/comments")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("아이디를 명시할 수 없습니다."));
        }

        @Test
        @DisplayName("실패 (게시글 아이디 누락)")
        void saveFailTest2() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            CommentRequestDto dto = CommentRequestDto.builder()
                    .content("content")
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/comments")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("질문글 번호가 누락되었습니다."));
        }
    }

    @Nested
    @DisplayName("댓글 수정 테스트")
    @WithMockCustomUser
    class updateCommentTest {

        @Test
        @DisplayName("성공")
        void updateSuccessTest1() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            CommentRequestDto dto = CommentRequestDto.builder()
                    .id(1L)
                    .content("content")
                    .questionId(123L)
                    .parentCommentId(111L)
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/comments")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("댓글 수정 완료"));
        }

        @Test
        @DisplayName("성공 (상위 댓글 번호 누락)")
        void updateSuccessTest2() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            CommentRequestDto dto = CommentRequestDto.builder()
                    .id(1L)
                    .content("content")
                    .questionId(123L)
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/comments")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("댓글 수정 완료"));
        }

        @Test
        @DisplayName("실패 (아이디 누락)")
        void updateFailTest1() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            CommentRequestDto dto = CommentRequestDto.builder()
                    .content("content")
                    .questionId(123L)
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/comments")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("아이디가 누락되었습니다."));
        }

        @Test
        @DisplayName("실패 (존재하지 않는 댓글 번호 or 존재하지 않는 질문글 번호)")
        void updateFailTest2() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            doThrow(new ResourceNotFoundException(NOT_FOUND_CONTENT_ID)).when(commentService).updateComment(any(), any());
            CommentRequestDto dto = CommentRequestDto.builder()
                    .id(1L)
                    .content("content")
                    .questionId(123L)
                    .build();

            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/comments")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_FOUND_CONTENT_ID));
        }
    }

    @Nested
    @DisplayName("댓글 삭제 테스트")
    @WithMockCustomUser
    class deleteCommentTest {
        @Test
        @DisplayName("성공")
        void deleteSuccessTest1() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));

            //when
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/comments/1")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );
            //then
            resultActions.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("댓글 삭제 완료"));
        }

        @Test
        @DisplayName("실패 (존재하지 않는 댓글 번호)")
        void deleteFailTest1() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            doThrow(new ResourceNotFoundException(NOT_FOUND_CONTENT_ID)).when(commentService).deleteComment(any(), any());

            //when
            ResultActions resultActions = mockMvc.perform(
                    delete("/api/comments/1")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );
            //then
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_FOUND_CONTENT_ID));
        }
    }


    User getTestUser() {
        return User.builder().id(1234L).nickname("test").role(USER).build();
    }

}