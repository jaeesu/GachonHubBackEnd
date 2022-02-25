package com.example.gachonhub.controller;

import com.example.gachonhub.config.SecurityConfig;
import com.example.gachonhub.domain.inquiry.PostInquiry;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.response.InquiryListResponseDto;
import com.example.gachonhub.payload.response.InquiryListResponseDto.ListInquiryDto;
import com.example.gachonhub.payload.response.InquiryResponseDto;
import com.example.gachonhub.service.InquiryService;
import com.example.gachonhub.support.WithMockCustomUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.example.gachonhub.domain.user.User.Role.USER;
import static com.example.gachonhub.util.ErrorUtil.NOT_FOUND_CONTENT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("prod")
@WebMvcTest(controllers = InquiryController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("문의글 api 테스트")
class InquiryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InquiryService inquiryService;

    @MockBean
    private UserRepository userRepository;

    @Nested
    @DisplayName("문의글 작성 테스트")
    @WithMockCustomUser
    class saveInquiryTest {

        @Test
        @DisplayName("성공")
        void saveSuccessTest1() throws Exception {
            //given
            MockMultipartFile file = new MockMultipartFile("testImage",
                    new FileInputStream((new ClassPathResource("test/testImage.jpeg")).getFile()));

            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when

            ResultActions perform = mockMvc.perform(
                    post("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .content(file.getBytes())
                            .param("title", "title")
                            .param("content", "content")
                            .param("password", "1234")
                            .param("secret", "true")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("문의글 작성 완료"));

        }

        @Test
        @DisplayName("성공 (파일 누락)")
        void saveSuccessTest2() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("title", "title")
                            .param("content", "content")
                            .param("password", "1234")
                            .param("secret", "true")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("문의글 작성 완료"));

        }

        @Test
        @DisplayName("성공 (비밀글)")
        void saveSuccessTest3() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("title", "title")
                            .param("content", "content")
                            .param("password", "1234")
                            .param("secret", "true")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("문의글 작성 완료"));

        }

        @Test
        @DisplayName("성공 (공개글)")
        void saveSuccessTest4() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("title", "title")
                            .param("content", "content")
                            .param("password", "1234")
                            .param("secret", "true")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("문의글 작성 완료"));

        }

        @Test
        @DisplayName("실패 (아이디 명시)")
        void saveFailTest1() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("id", "1")
                            .param("title", "title")
                            .param("content", "content")
                            .param("password", "1234")
                            .param("secret", "true")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("아이디를 명시할 수 없습니다."));
        }

        @Test
        @DisplayName("실패 (제목 누락)")
        void saveFailTest2() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("content", "content")
                            .param("password", "1234")
                            .param("secret", "true")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("제목이 누락되었습니다."));
        }

        @Test
        @DisplayName("실패 (내용 누락)")
        void saveFailTest3() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("title", "title")
                            .param("password", "1234")
                            .param("secret", "true")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );
            //Boolean이 아니라 boolean으로 두면 실패

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("내용이 누락되었습니다."));

        }

        @Test
        @DisplayName("실패 (비밀글 여부 누락)")
        void saveFailTest4() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("title", "title")
                            .param("content", "content")
                            .param("password", "1234")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("비밀글 여부가 누락되었습니다."));

        }

        @Test
        @DisplayName("실패 (비밀번호가 없는 비밀글)")
        void saveFailTest5() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("title", "title")
                            .param("content", "content")
                            .param("secret", "true")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest());

        }

        @Test
        @DisplayName("실패 (비밀번호가 있는 공개글)")
        void saveFailTest6() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("title", "title")
                            .param("content", "content")
                            .param("password", "1234")
                            .param("secret", "false")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            // TODO : error handling
            perform.andExpect(status().isBadRequest());

        }

        // 비밀글 여부와 비밀번호 유무 확인
    }

    @Nested
    @DisplayName("수정 테스트")
    @WithMockCustomUser
    class updateInquiryTest {

        //성공 테스트와 동일한 루트는 테스트 하지 않음 (id를 가지고 있는 것 외에 save와 다른 부분이 없음)
        //비밀글 여부를 바꿀 수 있는가?

        @Test
        @DisplayName("성공")
        void updateSuccessTest1() throws Exception {
            //given
            MockMultipartFile file = new MockMultipartFile("testImage",
                    new FileInputStream((new ClassPathResource("test/testImage.jpeg")).getFile()));

            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .content(file.getBytes())
                            .param("id", "1")
                            .param("title", "title")
                            .param("content", "content")
                            .param("password", "1234")
                            .param("secret", "true")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("문의글 수정 완료"));

        }

        @Test
        @DisplayName("성공 (파일 누락)")
        void updateSuccessTest2() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("id", "1")
                            .param("title", "title")
                            .param("content", "content")
                            .param("password", "1234")
                            .param("secret", "true")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("문의글 수정 완료"));
        }

        @Test
        @DisplayName("실패 (아이디 누락)")
        void updateFailTest1() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/posts/inquiry")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("title", "title")
                            .param("content", "content")
                            .param("password", "1234")
                            .param("secret", "true")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("아이디가 누락되었습니다."));
        }

    }

    @Nested
    @DisplayName("삭제 테스트")
    @WithMockCustomUser
    class deleteInquiryTest {

        @Test
        @DisplayName("성공")
        void deleteSuccessTest1() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));

            //when
            ResultActions perform = mockMvc.perform(
                    delete("/api/posts/inquiry/1")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("문의글 삭제 완료"));
        }

        @Test
        @DisplayName("실패 (존재하지 않는 게시글 번호)")
        void deleteFailTest1() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));
            doThrow(new ResourceNotFoundException(NOT_FOUND_CONTENT_ID)).when(inquiryService).deletePost(any(), any());

            //when
            ResultActions perform = mockMvc.perform(
                    delete("/api/posts/inquiry/1")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_FOUND_CONTENT_ID));
        }
    }


    //TODO : PERMITALL 필터링 되지 않는 부분 수정 (실제 api 요청 시에는 필터링이 잘 되는데 테스트에서는 되지 않음.)
    @Nested
    @DisplayName("조회 테스트")
    @WithMockCustomUser
    class findInquiryTest {

        @Test
        @DisplayName("특정글 조회 성공")
        void findSuccessTest1() throws Exception {
            //given
            String url = "https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg";
            InquiryResponseDto dto = InquiryResponseDto.builder()
                    .id(1L)
                    .user("test")
                    .title("title")
                    .content("content")
                    .img(url)
                    .writeAt(LocalDate.now())
                    .password(1234)
                    .secret(true)
                    .build();

            given(inquiryService.findById(any())).willReturn(dto);

            //when
            ResultActions perform = mockMvc.perform(
                    get("/api/posts/inquiry/1")
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.user").value(dto.getUser()))
                    .andExpect(jsonPath("$.data.title").value(dto.getTitle()))
                    .andExpect(jsonPath("$.data.content").value(dto.getContent()))
                    .andExpect(jsonPath("$.data.img").value(url))
                    .andExpect(jsonPath("$.data.writeAt").value(dto.getWriteAt().toString()))
                    .andExpect(jsonPath("$.data.password").value(dto.getPassword()))
                    .andExpect(jsonPath("$.data.secret").value(dto.isSecret()));

        }

        // TODO : 에러 해결하기
        @Test
        @DisplayName("전체 글 목록 조회 성공")
        void findSuccessTest2() throws Exception {
            //given
            ListInquiryDto listInquiryDto1 = new ListInquiryDto(1L, "user", "title");
            ListInquiryDto listInquiryDto2 = new ListInquiryDto(1L, "user", "title");
            ListInquiryDto listInquiryDto3 = new ListInquiryDto(1L, "user", "title");
            ListInquiryDto listInquiryDto4 = new ListInquiryDto(1L, "user", "title");
            List<ListInquiryDto> list = new ArrayList<>();
            list.add(listInquiryDto1);
            list.add(listInquiryDto2);
            list.add(listInquiryDto3);
            list.add(listInquiryDto4);

            InquiryListResponseDto dto = new InquiryListResponseDto(1, 1, list);

            //any() => anyInt()
            given(inquiryService.findAllByPage(anyInt())).willReturn(dto);

            //when
            ResultActions page = mockMvc.perform(
                    get("/api/posts/inquiry")
                            .param("page", "0")
            );

            //then
            page.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.data.size()").value(list.size()));

        }


        @Test
        @DisplayName("특정글 조회 실패 (존재하지 않는 게시글 번호)")
        void findFailTest1() throws Exception {
            //given
            doThrow(new ResourceNotFoundException(NOT_FOUND_CONTENT_ID)).when(inquiryService).findById(any());

            //when
            ResultActions perform = mockMvc.perform(
                    get("/api/posts/inquiry/1")
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_FOUND_CONTENT_ID));
        }
    }

    User getTestUser() {
        return User.builder().id(1234L).nickname("test").role(USER).build();
    }

}