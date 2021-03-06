//package com.example.gachonhub.controller;
//
//import com.example.gachonhub.config.SecurityConfig;
//import com.example.gachonhub.domain.team.Team;
//import com.example.gachonhub.domain.user.User;
//import com.example.gachonhub.domain.user.UserRepository;
//import com.example.gachonhub.domain.user.relation.UserToTeam;
//import com.example.gachonhub.exception.NotAccessUserException;
//import com.example.gachonhub.exception.ResourceNotFoundException;
//import com.example.gachonhub.team.ui.dto.TeamAddMemberRequestDto;
//import com.example.gachonhub.team.ui.dto.TeamResponseDto;
//import com.example.gachonhub.team.application.TeamService;
//import com.example.gachonhub.support.WithMockCustomUser;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//
//import java.io.FileInputStream;
//import java.util.Optional;
//
//import static com.example.gachonhub.domain.user.User.Role.USER;
//import static com.example.gachonhub.common.exception.ErrorUtil.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.doThrow;
//import static org.springframework.http.MediaType.APPLICATION_JSON;
//import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ActiveProfiles("local")
//@WebMvcTest(controllers = TeamController.class,
//        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
//@DisplayName("team api ?????????")
//class TeamControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Autowired
//    private WebApplicationContext wac;
//
//
//    @BeforeEach
//    public void setup() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
//                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // ?????? ??????
//                .alwaysDo(print())
//                .build();
//    }
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @MockBean
//    private TeamService teamService;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Nested
//    @DisplayName("??? ?????? ?????????")
//    @WithMockCustomUser
//    class createTeamTest {
//        @Test
//        @DisplayName("??????")
//        void createSuccessTest() throws Exception {
//            //given
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            MockMultipartFile file = new MockMultipartFile("testImage",
//                    new FileInputStream((new ClassPathResource("test/testImage.jpeg")).getFile()));
//
//            //when
//            ResultActions perform = mockMvc.perform(
//                    post("/api/groups")
//                            .contentType(MULTIPART_FORM_DATA)
//                            .content(file.getBytes())
//                            .param("name", "????????????")
//                            .param("field", "????????????")
//                            .param("people", "5")
//                            .param("repos", "http://github.com")
//                            .param("type", "CREW")
//                            .param("recruiting", "false")
//                            .param("description", "?????? ???")
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            perform.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value(200))
//                    .andExpect(jsonPath("$.data").value("??? ?????? ??????"));
//        }
//
//        @Test
//        @DisplayName("?????? (????????? ??????)")
//        void createSuccessTest2() throws Exception {
//            //given
//
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            //when
//            ResultActions perform = mockMvc.perform(
//                    post("/api/groups")
//                            .contentType(MULTIPART_FORM_DATA)
//                            .param("name", "????????????")
//                            .param("field", "????????????")
//                            .param("people", "5")
//                            .param("repos", "http://github.com")
//                            .param("type", "CREW")
//                            .param("recruiting", "false")
//                            .param("description", "?????? ???")
//                            .param("description", "?????? ???")
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            perform.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value(200))
//                    .andExpect(jsonPath("$.data").value("??? ?????? ??????"));
//        }
//
//        @Test
//        @DisplayName("?????? (????????? ??????)")
//        void createFailTest1() throws Exception {
//            //given
//
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            //when
//            ResultActions perform = mockMvc.perform(
//                    post("/api/groups")
//                            .contentType(MULTIPART_FORM_DATA)
//                            .param("teamId", "1")
//                            .param("name", "????????????")
//                            .param("field", "????????????")
//                            .param("people", "5")
//                            .param("recruiting", "false")
//                            .param("description", "?????? ???")
//                            .param("repos", "http://github.com")
//                            .param("type", "CREW")
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            perform.andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value(400))
//                    .andExpect(jsonPath("$.message").value("?????? ???????????? ????????? ??? ????????????."));
//        }
//
//        @Test
//        @DisplayName("?????? (?????? ??????)")
//        void createFailTest2() throws Exception {
//            //given
//
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            //when
//            ResultActions perform = mockMvc.perform(
//                    post("/api/groups")
//                            .contentType(MULTIPART_FORM_DATA)
//                            .param("field", "????????????")
//                            .param("people", "5")
//                            .param("repos", "http://github.com")
//                            .param("type", "CREW")
//                            .param("recruiting", "false")
//                            .param("description", "?????? ???")
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            perform.andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value(400))
//                    .andExpect(jsonPath("$.message").value("?????? ????????? ?????????????????????."));
//        }
//
//    }
//
//    @Nested
//    @DisplayName("??? ?????? ?????? ?????????")
//    class readTeamTest {
//
//        @Test
//        @DisplayName("?????? ??? ?????? ?????????")
//        void readSuccessTest1() throws Exception {
//            //given
//            UserToTeam testRelation = getTestRelation();
//            TeamResponseDto dto = TeamResponseDto.fromEntity(testRelation.getTeam());
//            given(teamService.findTeam(1111L)).willReturn(dto);
//
//            //when
//            ResultActions res = mockMvc.perform(
//                    get("/api/groups/1111")
//                            .contentType(APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(dto))
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            res.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value(200))
//                    .andExpect(jsonPath("$.data.id").value(dto.getId()))
//                    .andExpect(jsonPath("$.data.name").value(dto.getName()))
//                    .andExpect(jsonPath("$.data.authorId").value(dto.getAuthorId()))
//                    .andExpect(jsonPath("$.data.field").value(dto.getField()))
//                    .andExpect(jsonPath("$.data.users.size()").value(testRelation.getTeam().getUsers().size()));
//        }
//
//    }
//
//    @Nested
//    @DisplayName("??? ?????? ?????? ?????????")
//    @WithMockCustomUser
//    class updateTeamTest {
//        @Test
//        @DisplayName("??????")
//        void updateSuccessTest() throws Exception {
//            //given
//
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            MockMultipartFile file = new MockMultipartFile("testImage",
//                    new FileInputStream((new ClassPathResource("test/testImage.jpeg")).getFile()));
//
//            //when
//            ResultActions perform = mockMvc.perform(
//                    put("/api/groups")
//                            .contentType(MULTIPART_FORM_DATA)
//                            .content(file.getBytes())
//                            .param("teamId", "1")
//                            .param("name", "????????????")
//                            .param("field", "????????????")
//                            .param("people", "5")
//                            .param("repos", "http://github.com")
//                            .param("description", "???????????? ?????????")
//                            .param("type", "CREW")
//                            .param("recruiting", "true")
//                            .param("recruitingContent", "???????????????. ???????????????.")
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            perform.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value(200))
//                    .andExpect(jsonPath("$.data").value("??? ?????? ?????? ??????"));
//
//        }
//
//        @Test
//        @DisplayName("?????? (????????? ??????)")
//        void updateSuccessTest2() throws Exception {
//            //given
//
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            //when
//            ResultActions perform = mockMvc.perform(
//                    put("/api/groups")
//                            .contentType(MULTIPART_FORM_DATA)
//                            .param("teamId", "1")
//                            .param("name", "????????????")
//                            .param("field", "????????????")
//                            .param("people", "5")
//                            .param("repos", "http://github.com")
//                            .param("description", "???????????? ?????????")
//                            .param("type", "CREW")
//                            .param("recruiting", "true")
//                            .param("recruitingContent", "???????????????. ???????????????.")
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            perform.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value(200))
//                    .andExpect(jsonPath("$.data").value("??? ?????? ?????? ??????"));
//        }
//
//        @Test
//        @DisplayName("?????? (????????? ?????????)")
//        void updateFailTest1() throws Exception {
//            //given
//
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            //when
//            ResultActions perform = mockMvc.perform(
//                    put("/api/groups")
//                            .contentType(MULTIPART_FORM_DATA)
//                            .param("name", "????????????")
//                            .param("field", "????????????")
//                            .param("people", "5")
//                            .param("repos", "http://github.com")
//                            .param("description", "???????????? ?????????")
//                            .param("type", "CREW")
//                            .param("recruiting", "true")
//                            .param("recruitingContent", "???????????????. ???????????????.")
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            perform.andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value(400))
//                    .andExpect(jsonPath("$.message").value("?????? ???????????? ?????????????????????."));
//        }
//
//        @Test
//        @DisplayName("?????? (?????? ??????)")
//        void updateFailTest2() throws Exception {
//            //given
//
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            //when
//            ResultActions perform = mockMvc.perform(
//                    put("/api/groups")
//                            .contentType(MULTIPART_FORM_DATA)
//                            .param("teamId", "1")
//                            .param("field", "????????????")
//                            .param("people", "5")
//                            .param("repos", "http://github.com")
//                            .param("description", "???????????? ?????????")
//                            .param("type", "CREW")
//                            .param("recruiting", "true")
//                            .param("recruitingContent", "???????????????. ???????????????.")
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            perform.andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value(400))
//                    .andExpect(jsonPath("$.message").value("?????? ????????? ?????????????????????."));
//        }
//
//    }
//
//    @Nested
//    @DisplayName("??? ?????? ?????????")
//    @WithMockCustomUser
//    class DeleteTeamTest {
//
//        @Test
//        @DisplayName("??????")
//        void deleteSuccessTest() throws Exception {
//            //given
//
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            //when
//            ResultActions res = mockMvc.perform(
//                    delete("/api/groups/1")
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            res.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value(200))
//                    .andExpect(jsonPath("$.data").value("??? ?????? ??????"));
//        }
//
//        @Test
//        @DisplayName("?????? (query ????????? ??????)")
//        void deleteFailTest1() throws Exception {
//            //given
//
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            //when
//            ResultActions res = mockMvc.perform(
//                    delete("/api/groups")
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            res.andExpect(status().isMethodNotAllowed());
//        }
//
//        @Test
//        @DisplayName("?????? (???????????? ?????? ?????? ?????????)")
//        void deleteFailTest2() throws Exception {
//            //given
//            doThrow(new ResourceNotFoundException(NOT_FOUND_CONTENT_ID)).when(teamService).deleteTeam(any(), any());
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            //when
//            ResultActions res = mockMvc.perform(
//                    delete("/api/groups/1")
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            res.andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.message").value(NOT_FOUND_CONTENT_ID));
//        }
//    }
//
//    @Nested
//    @DisplayName("?????? ?????? ?????????")
//    @WithMockCustomUser
//    class addMemberTest {
//        @Test
//        @DisplayName("??????")
//        void addMemberSuccessTest() throws Exception {
//            //given
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
//                    .memberNickName("test")
//                    .teamId(1L)
//                    .build();
//            //when
//            ResultActions res = mockMvc.perform(
//                    post("/api/groups/member")
//                            .contentType(APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(dto))
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            res.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value(200))
//                    .andExpect(jsonPath("$.data").value("??? ?????? ?????? ??????"));
//        }
//
//        @Test
//        @DisplayName("?????? (?????? ????????? ?????? ?????????)")
//        void addMemberFailTest1() throws Exception {
//            //given
//            User testUser = getTestUser();
//            given(userRepository.findById(any())).willReturn(Optional.of(testUser));
//            doThrow(new NotAccessUserException(NOT_CORRECT_USER_ID)).when(teamService).addMember(testUser, any(), anyLong());
//            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
//                    .memberNickName("test")
//                    .teamId(1L)
//                    .build();
//            //when
//            ResultActions res = mockMvc.perform(
//                    post("/api/groups/member")
//                            .contentType(APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(dto))
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            res.andExpect(status().isUnauthorized())
//                    .andExpect(jsonPath("$.status").value(401))
//                    .andExpect(jsonPath("$.message").value(NOT_CORRECT_USER_ID));
//
//        }
//
//        @Test
//        @DisplayName("?????? (?????? ?????? ?????? ?????? ?????????)")
//        void addMemberFailTest2() throws Exception {
//            //given
//            User testUser = getTestUser();
//            given(userRepository.findById(any())).willReturn(Optional.of(testUser));
//            doThrow(new ResourceNotFoundException(NOT_FOUND_GROUP_ID)).when(teamService).addMember(testUser, any(), any());
//            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
//                    .memberNickName("test")
//                    .teamId(1L)
//                    .build();
//            //when
//            ResultActions res = mockMvc.perform(
//                    post("/api/groups/member")
//                            .contentType(APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(dto))
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            res.andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value(400))
//                    .andExpect(jsonPath("$.message").value(NOT_FOUND_GROUP_ID));
//        }
//
//        @Test
//        @DisplayName("?????? (????????? ?????? ?????? ?????? ????????? ?????????)")
//        void addMemberFailTest3() throws Exception {
//            //given
//            User testUser = getTestUser();
//            given(userRepository.findById(any())).willReturn(Optional.of(testUser));
//            doThrow(new ResourceNotFoundException(NOT_FOUND_USER_ID_IN_GROUP)).when(teamService).addMember(testUser,anyString(), anyLong());
//            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
//                    .memberNickName("test")
//                    .teamId(1L)
//                    .build();
//            //when
//            ResultActions res = mockMvc.perform(
//                    post("/api/groups/member")
//                            .contentType(APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(dto))
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            res.andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value(400))
//                    .andExpect(jsonPath("$.message").value(NOT_FOUND_USER_ID_IN_GROUP));
//        }
//
//        @Test
//        @DisplayName("?????? (????????? ????????? ????????? ??????)")
//        void addMemberFailTest4() throws Exception {
//            //given
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
//                    .teamId(1L)
//                    .build();
//            //when
//            ResultActions res = mockMvc.perform(
//                    post("/api/groups/member")
//                            .contentType(APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(dto))
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            res.andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value(400))
//                    .andExpect(jsonPath("$.message").value("????????? ????????? ???????????? ?????????????????????."));
//        }
//
//        @Test
//        @DisplayName("?????? (????????? ?????? ????????? ??????)")
//        void addMemberFailTest5() throws Exception {
//            //given
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
//                    .memberNickName("test")
//                    .build();
//            //when
//            ResultActions res = mockMvc.perform(
//                    post("/api/groups/member")
//                            .contentType(APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(dto))
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            res.andExpect(status().isBadRequest())
//                    .andExpect(jsonPath("$.status").value(400))
//                    .andExpect(jsonPath("$.message").value("???????????? ????????? ??? ???????????? ?????????????????????."));
//        }
//    }
//
//    @Nested
//    @DisplayName("?????? ?????? ?????????")
//    @WithMockCustomUser
//    class DeleteMemberTest {
//        @Test
//        @DisplayName("??????")
//        void deleteMemberSuccessTest() throws Exception {
//            //given
//            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
//            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
//                    .memberNickName("test")
//                    .teamId(1L)
//                    .build();
//
//            //when
//            ResultActions res = mockMvc.perform(
//                    delete("/api/groups/member")
//                            .contentType(APPLICATION_JSON)
//                            .content(objectMapper.writeValueAsString(dto))
//                            .with(SecurityMockMvcRequestPostProcessors.csrf())
//            );
//
//            //then
//            res.andExpect(status().isOk())
//                    .andExpect(jsonPath("$.status").value(200))
//                    .andExpect(jsonPath("$.data").value("??? ?????? ?????? ??????"));
//        }
//
//    }
//
//
//    User getTestUser() {
//        User test = User.builder()
//                .id(1234L)
//                .nickname("test")
//                .role(USER)
//                .avatarUrl("http://github.com")
//                .build();
//        return test;
//    }
//
//    Team getTestTeam() {
//        Team team = Team.builder()
//                .id(1111L)
//                .name("test")
//                .authorId(1234L)
//                .field("coding")
//                .people(5)
//                .repos("http://repos.com")
//                .type(Team.TeamType.CREW)
//                .avatarUrl("http://repos.com")
//                .build();
//        return team;
//    }
//
//    UserToTeam getTestRelation() {
//        User testUser = getTestUser();
//        Team testTeam = getTestTeam();
//
//        UserToTeam build = UserToTeam.builder()
//                .user(testUser)
//                .team(testTeam)
//                .build();
//
//        testTeam.getUsers().add(build);
//        testUser.getGroups().add(build);
//
//        return build;
//    }
//
//
//}