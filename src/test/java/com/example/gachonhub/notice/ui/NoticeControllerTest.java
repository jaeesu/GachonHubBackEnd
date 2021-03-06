package com.example.gachonhub.notice.ui;

import com.example.gachonhub.common.config.SecurityConfig;
import com.example.gachonhub.notice.ui.NoticeController;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.notice.ui.dto.NoticeRequestDto;
import com.example.gachonhub.notice.ui.dto.NoticeResponseDto;
import com.example.gachonhub.notice.application.NoticeService;
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

import java.time.LocalDate;
import java.util.Optional;

import static com.example.gachonhub.user.domain.User.Role.USER;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_CONTENT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@WebMvcTest(controllers = NoticeController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("???????????? api ?????????")
class NoticeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private NoticeService noticeService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("???????????? ?????? ?????????")
    @WithMockCustomUser
    class NoticeSaveTest {

        @Test
        @DisplayName("??????")
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
        @DisplayName("?????? (?????? ??????)")
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
                    .andExpect(jsonPath("$.message").value("????????? ?????????????????????."));
        }

        @Test
        @DisplayName("?????? (?????? ??????)")
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
                    .andExpect(jsonPath("$.message").value("????????? ?????????????????????."));
        }

    }

    //TODO : PERMITALL ????????? ?????? ?????? ?????? ?????? (?????? api ?????? ????????? ???????????? ??? ????????? ?????????????????? ?????? ??????.)
    @Nested
    @DisplayName("???????????? ?????? ?????????")
    @WithMockCustomUser
    class ReadNoticeTest {

        @Test
        @DisplayName("?????? ??? ?????? ??????")
        void readSuccessTest1() throws Exception {
            //given

            NoticeResponseDto dto = NoticeResponseDto.builder()
                    .id(1L)
                    .user("testUser")
                    .title("title")
                    .content("content")
                    .writeAt(LocalDate.now())
                    .build();
            given(noticeService.findNoticePost(any())).willReturn(dto);

            //when

            ResultActions perform = mockMvc.perform(
                    get("/api/posts/notice/1")
//                            .with(anonymous())
            );

            //then
            //mockmvctest?????? permitall??? ???????????? ?????????.
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(dto.getId()))
                    .andExpect(jsonPath("$.data.user").value(dto.getUser()))
                    .andExpect(jsonPath("$.data.title").value(dto.getTitle()))
                    .andExpect(jsonPath("$.data.content").value(dto.getContent()))
                    .andExpect(jsonPath("$.data.writeAt").value(dto.getWriteAt().toString()));

        }

        @Test
        @DisplayName("?????? ??? ?????? ?????? (???????????? ?????? ???)")
        void readFailTest1() throws Exception {
            //given
            given(noticeService.findNoticePost(any())).willThrow(new ResourceNotFoundException(NOT_FOUND_CONTENT_ID));

            //when
            ResultActions perform = mockMvc.perform(
                    get("/api/posts/notice/1")
            );

            //then
            perform.andExpect(status().isBadRequest());
        }
    }

    @Nested
    @WithMockCustomUser
    @DisplayName("???????????? ?????? ?????????")
    class UpdateNoticeTest {

        @Test
        @DisplayName("??????")
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
        @DisplayName("?????? (????????? ??????)")
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
                    .andExpect(jsonPath("$.message").value("???????????? ?????????????????????."));
        }

        @Test
        @DisplayName("?????? (?????? ??????)")
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
                    .andExpect(jsonPath("$.message").value("????????? ?????????????????????."));
        }

        @Test
        @DisplayName("?????? (?????? ??????)")
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
                    .andExpect(jsonPath("$.message").value("????????? ?????????????????????."));
        }
    }

    @Nested
    @DisplayName("???????????? ?????? ?????????")
    @WithMockCustomUser
    class DeleteNoticeTest {

        @Test
        @DisplayName("??????")
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
                    .andExpect(jsonPath("$.data").value("???????????? ?????? ??????"));
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