package com.example.gachonhub.controller;

import com.example.gachonhub.config.SecurityConfig;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.payload.request.ReposRequestDto;
import com.example.gachonhub.payload.request.UserInfoRequestDto;
import com.example.gachonhub.payload.request.UserInfoRequestDto.UserSnsDto;
import com.example.gachonhub.payload.response.UserResponseDto;
import com.example.gachonhub.service.UserReposService;
import com.example.gachonhub.service.UserService;
import com.example.gachonhub.support.WithMockCustomUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.BDDAssumptions;
import org.junit.jupiter.api.*;
import org.mockito.BDDMockito;
import org.mockito.MockedStatic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import springfox.documentation.builders.PathSelectors;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("prod")
@WebMvcTest(controllers = UserController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("사용자 api 테스트")
class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserService userService;

    @MockBean
    private UserReposService reposService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
    }

    @Nested
    @DisplayName("사용자 정보 수정 테스트")
    @WithMockCustomUser
    class UpdateUserTest {

        @Test
        @DisplayName("성공")
        void updateSuccessTest() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));
            UserInfoRequestDto dto = UserInfoRequestDto.builder()
                    .major("컴퓨터공학")
                    .graduate(true)
                    .build();

            //when
            ResultActions res = mockMvc.perform(
                    put("/api/me")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("사용자 정보 수정 완료"));
        }

        @Test
        @DisplayName("성공 (major만)")
        void updateSuccessTest2() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));
            UserInfoRequestDto dto = UserInfoRequestDto.builder()
                    .major("컴퓨터공학")
                    .build();

            //when
            ResultActions res = mockMvc.perform(
                    put("/api/me")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("사용자 정보 수정 완료"));
        }

        @Test
        @DisplayName("성공 (graduate만)")
        void updateSuccessTest3() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));
            UserInfoRequestDto dto = UserInfoRequestDto.builder()
                    .graduate(true)
                    .build();

            //when
            ResultActions res = mockMvc.perform(
                    put("/api/me")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("사용자 정보 수정 완료"));
        }

    }

    @Nested
    @DisplayName("사용자 대표 레포지토리 수정 테스트")
    @WithMockCustomUser
    class updateUserReposTest {
        @Test
        @DisplayName("성공")
        void updateSuccessTest() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));
            ReposRequestDto dto = ReposRequestDto.builder()
                    .repos(Arrays.asList(1L, 2L, 3L))
                    .build();

            //when
            ResultActions res = mockMvc.perform(
                    put("/api/me/repos")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("사용자 대표 레포지토리 수정 완료"));
        }

        @Test
        @DisplayName("실패 (sns 4개 이상)")
        void updateFailTest() throws Exception {
            //given
            User testUser = getTestUser();
            given(userRepository.findById(any())).willReturn(Optional.of(testUser));
            ReposRequestDto dto = ReposRequestDto.builder()
                    .repos(Arrays.asList(1L, 2L, 3L, 4L))
                    .build();

            //when
            ResultActions res = mockMvc.perform(
                    put("/api/me/repos")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("대표 레포지토리는 3개까지 지정할 수 있습니다."));
        }
    }

    User getTestUser() {
        User user = User.builder()
                .id(1234L)
                .nickname("test")
                .role(User.Role.USER)
                .graduate(true)
                .avatarUrl("http://github.com")
                .build();
        return user;
    }
}