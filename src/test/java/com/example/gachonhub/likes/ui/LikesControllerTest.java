package com.example.gachonhub.likes.ui;

import com.example.gachonhub.common.config.SecurityConfig;
import com.example.gachonhub.likes.ui.LikesController;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.likes.ui.dto.LikesRequestDto;
import com.example.gachonhub.likes.application.LikesService;
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

import static com.example.gachonhub.likes.domain.Likes.Type.CONTEST;
import static com.example.gachonhub.user.domain.User.Role.USER;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_CONTENT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@WebMvcTest(controllers = LikesController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("공감 api 테스트")
class LikesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private LikesService likesService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("공감 추가 테스트")
    @WithMockCustomUser
    class addLikesTest {

        @Test
        @DisplayName("성공")
        void addSuccessTest1() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            LikesRequestDto dto = LikesRequestDto.builder().type(CONTEST).id(1L).build();

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/likes")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("좋아요 반응 추가 완료"));
        }

        @Test
        @DisplayName("실패 (상위 타입 누락)")
        void addFailTest1() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            LikesRequestDto dto = LikesRequestDto.builder().id(1L).build();

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/likes")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("상위 타입이 누락되었습니다."));
        }

        @Test
        @DisplayName("실패 (상위 아이디 누락)")
        void addFailTest2() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            LikesRequestDto dto = LikesRequestDto.builder().type(CONTEST).build();

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/likes")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("상위 아이디가 누락되었습니다."));
        }

        @Test
        @DisplayName("실패 (존재하지 않는 상위 타입)")
        void addFailTest3() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            LikesRequestDto dto = LikesRequestDto.builder().type(CONTEST).id(1L).build();

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/likes")
                            .contentType(APPLICATION_JSON)
                            .content("{\"type\" : \"test\"," +
                                    "\"id\" : \"1\"}")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            //httpMessageNotReadableException
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message", "JSON parse error").exists());

        }

        @Test
        @DisplayName("실패 (존재하지 않는 상위 아이디)")
        void addFailTest4() throws Exception {
            //given
            LikesRequestDto dto = LikesRequestDto.builder().type(CONTEST).id(1L).build();
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            doThrow(new ResourceNotFoundException(NOT_FOUND_CONTENT_ID)).when(likesService).saveLikes(any(), any());

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/likes")
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
    @DisplayName("공감 삭제 테스트")
    @WithMockCustomUser
    class deleteLikesTest {

        // 실패 테스트는 add test와 동일

        @Test
        @DisplayName("성공")
        void deleteSuccessTest1() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            LikesRequestDto dto = LikesRequestDto.builder().type(CONTEST).id(1L).build();

            //when
            ResultActions perform = mockMvc.perform(
                    delete("/api/likes")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("좋아요 반응 삭제 완료"));
        }

    }

    User getTestUser() {
        return User.builder().id(1234L).nickname("test").role(USER).build();
    }

}