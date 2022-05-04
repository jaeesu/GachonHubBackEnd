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

    //TODO : PERMITALL 필터링 되지 않는 부분 수정 (실제 api 요청 시에는 필터링이 잘 되는데 테스트에서는 되지 않음.)
    @Nested
    @DisplayName("공지사항 조회 테스트")
    @WithMockCustomUser
    class ReadNoticeTest {

        @Test
        @DisplayName("특정 글 조회 성공")
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
            //mockmvctest에서 permitall이 작동하지 않는다.
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(dto.getId()))
                    .andExpect(jsonPath("$.data.user").value(dto.getUser()))
                    .andExpect(jsonPath("$.data.title").value(dto.getTitle()))
                    .andExpect(jsonPath("$.data.content").value(dto.getContent()))
                    .andExpect(jsonPath("$.data.writeAt").value(dto.getWriteAt().toString()));

        }

        @Test
        @DisplayName("특정 글 조회 실패 (존재하지 않는 글)")
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