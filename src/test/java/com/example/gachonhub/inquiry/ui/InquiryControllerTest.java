package com.example.gachonhub.inquiry.ui;

import com.example.gachonhub.common.config.SecurityConfig;
import com.example.gachonhub.inquiry.ui.InquiryController;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.inquiry.ui.dto.InquiryListResponseDto;
import com.example.gachonhub.inquiry.ui.dto.InquiryListResponseDto.ListInquiryDto;
import com.example.gachonhub.inquiry.ui.dto.InquiryResponseDto;
import com.example.gachonhub.inquiry.application.InquiryService;
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
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.gachonhub.user.domain.User.Role.USER;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_CONTENT_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@WebMvcTest(controllers = InquiryController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("????????? api ?????????")
class InquiryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InquiryService inquiryService;

    @MockBean
    private UserRepository userRepository;

    @Nested
    @DisplayName("????????? ?????? ?????????")
    @WithMockCustomUser
    class saveInquiryTest {

        @Test
        @DisplayName("??????")
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
                    .andExpect(jsonPath("$.data").value("????????? ?????? ??????"));

        }

        @Test
        @DisplayName("?????? (?????? ??????)")
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
                    .andExpect(jsonPath("$.data").value("????????? ?????? ??????"));

        }

        @Test
        @DisplayName("?????? (?????????)")
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
                    .andExpect(jsonPath("$.data").value("????????? ?????? ??????"));

        }

        @Test
        @DisplayName("?????? (?????????)")
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
                    .andExpect(jsonPath("$.data").value("????????? ?????? ??????"));

        }

        @Test
        @DisplayName("?????? (????????? ??????)")
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
                    .andExpect(jsonPath("$.message").value("???????????? ????????? ??? ????????????."));
        }

        @Test
        @DisplayName("?????? (?????? ??????)")
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
                    .andExpect(jsonPath("$.message").value("????????? ?????????????????????."));
        }

        @Test
        @DisplayName("?????? (?????? ??????)")
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
            //Boolean??? ????????? boolean?????? ?????? ??????

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("????????? ?????????????????????."));

        }

        @Test
        @DisplayName("?????? (????????? ?????? ??????)")
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
                    .andExpect(jsonPath("$.message").value("????????? ????????? ?????????????????????."));

        }

        @Test
        @DisplayName("?????? (??????????????? ?????? ?????????)")
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
        @DisplayName("?????? (??????????????? ?????? ?????????)")
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

        // ????????? ????????? ???????????? ?????? ??????
    }

    @Nested
    @DisplayName("?????? ?????????")
    @WithMockCustomUser
    class updateInquiryTest {

        //?????? ???????????? ????????? ????????? ????????? ?????? ?????? (id??? ????????? ?????? ??? ?????? save??? ?????? ????????? ??????)
        //????????? ????????? ?????? ??? ??????????

        @Test
        @DisplayName("??????")
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
                    .andExpect(jsonPath("$.data").value("????????? ?????? ??????"));

        }

        @Test
        @DisplayName("?????? (?????? ??????)")
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
                    .andExpect(jsonPath("$.data").value("????????? ?????? ??????"));
        }

        @Test
        @DisplayName("?????? (????????? ??????)")
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
                    .andExpect(jsonPath("$.message").value("???????????? ?????????????????????."));
        }

    }

    @Nested
    @DisplayName("?????? ?????????")
    @WithMockCustomUser
    class deleteInquiryTest {

        @Test
        @DisplayName("??????")
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
                    .andExpect(jsonPath("$.data").value("????????? ?????? ??????"));
        }

        @Test
        @DisplayName("?????? (???????????? ?????? ????????? ??????)")
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


    //TODO : PERMITALL ????????? ?????? ?????? ?????? ?????? (?????? api ?????? ????????? ???????????? ??? ????????? ?????????????????? ?????? ??????.)
    @Nested
    @DisplayName("?????? ?????????")
    @WithMockCustomUser
    class findInquiryTest {

        @Test
        @DisplayName("????????? ?????? ??????")
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

        // TODO : ?????? ????????????
        @Test
        @DisplayName("?????? ??? ?????? ?????? ??????")
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
        @DisplayName("????????? ?????? ?????? (???????????? ?????? ????????? ??????)")
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