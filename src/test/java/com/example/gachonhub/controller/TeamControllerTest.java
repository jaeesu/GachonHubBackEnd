package com.example.gachonhub.controller;

import com.example.gachonhub.config.SecurityConfig;
import com.example.gachonhub.domain.team.Team;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.domain.user.relation.UserToTeam;
import com.example.gachonhub.exception.NotAccessUserException;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.request.TeamAddMemberRequestDto;
import com.example.gachonhub.payload.request.TeamContentRequestDto;
import com.example.gachonhub.payload.response.TeamListResponseDto;
import com.example.gachonhub.payload.response.TeamResponseDto;
import com.example.gachonhub.service.TeamService;
import com.example.gachonhub.support.WithMockCustomUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.io.FileInputStream;
import java.util.Optional;

import static com.example.gachonhub.domain.user.User.Role.USER;
import static com.example.gachonhub.util.ErrorUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("prod")
@WebMvcTest(controllers = TeamController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = SecurityConfig.class)})
@DisplayName("team api 테스트")
class TeamControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;


    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))  // 필터 추가
                .alwaysDo(print())
                .build();
    }

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TeamService teamService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Nested
    @DisplayName("팀 생성 테스트")
    @WithMockCustomUser
    class createTeamTest {
        @Test
        @DisplayName("성공")
        void createSuccessTest() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            MockMultipartFile file = new MockMultipartFile("testImage",
                    new FileInputStream((new ClassPathResource("test/testImage.jpeg")).getFile()));

            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/groups")
                            .contentType(MULTIPART_FORM_DATA)
                            .content(file.getBytes())
                            .param("name", "테스트팀")
                            .param("field", "정보보안")
                            .param("people", "5")
                            .param("repos", "http://github.com")
                            .param("type", "CREW")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("팀 생성 성공"));
        }

        @Test
        @DisplayName("성공 (이미지 누락)")
        void createSuccessTest2() throws Exception {
            //given

            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/groups")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("name", "테스트팀")
                            .param("field", "정보보안")
                            .param("people", "5")
                            .param("repos", "http://github.com")
                            .param("type", "CREW")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("팀 생성 성공"));
        }

        @Test
        @DisplayName("실패 (아이디 포함)")
        void createFailTest1() throws Exception {
            //given

            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/groups")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("teamId", "1")
                            .param("name", "테스트팀")
                            .param("field", "정보보안")
                            .param("people", "5")
                            .param("repos", "http://github.com")
                            .param("type", "CREW")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("그룹 아이디를 명시할 수 없습니다."));
        }

        @Test
        @DisplayName("실패 (이름 누락)")
        void createFailTest2() throws Exception {
            //given

            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            //when
            ResultActions perform = mockMvc.perform(
                    post("/api/groups")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("field", "정보보안")
                            .param("people", "5")
                            .param("repos", "http://github.com")
                            .param("type", "CREW")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("그룹 이름이 누락되었습니다."));
        }

    }

    @Nested
    @DisplayName("팀 정보 조회 테스트")
    class readTeamTest {

        @Test
        @DisplayName("특정 팀 조회 테스트")
        void readSuccessTest1() throws Exception {
            //given
            UserToTeam testRelation = getTestRelation();
            TeamResponseDto dto = TeamResponseDto.fromEntity(testRelation.getTeam());
            given(teamService.findTeam(1111L)).willReturn(dto);

            //when
            ResultActions res = mockMvc.perform(
                    get("/api/groups/1111")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data.id").value(dto.getId()))
                    .andExpect(jsonPath("$.data.name").value(dto.getName()))
                    .andExpect(jsonPath("$.data.authorId").value(dto.getAuthorId()))
                    .andExpect(jsonPath("$.data.field").value(dto.getField()))
                    .andExpect(jsonPath("$.data.users.size()").value(testRelation.getTeam().getUsers().size()));
        }

    }

    @Nested
    @DisplayName("팀 정보 수정 테스트")
    @WithMockCustomUser
    class updateTeamTest {
        @Test
        @DisplayName("성공")
        void updateSuccessTest() throws Exception {
            //given

            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            MockMultipartFile file = new MockMultipartFile("testImage",
                    new FileInputStream((new ClassPathResource("test/testImage.jpeg")).getFile()));

            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/groups")
                            .contentType(MULTIPART_FORM_DATA)
                            .content(file.getBytes())
                            .param("teamId", "1")
                            .param("name", "테스트팀")
                            .param("field", "정보보안")
                            .param("people", "5")
                            .param("repos", "http://github.com")
                            .param("type", "CREW")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("팀 정보 수정 완료"));
        }

        @Test
        @DisplayName("성공 (이미지 누락)")
        void updateSuccessTest2() throws Exception {
            //given

            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/groups")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("teamId", "1")
                            .param("name", "테스트팀")
                            .param("field", "정보보안")
                            .param("people", "5")
                            .param("repos", "http://github.com")
                            .param("type", "CREW")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("팀 정보 수정 완료"));
        }

        @Test
        @DisplayName("실패 (아이디 미포함)")
        void updateFailTest1() throws Exception {
            //given

            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/groups")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("name", "테스트팀")
                            .param("field", "정보보안")
                            .param("people", "5")
                            .param("repos", "http://github.com")
                            .param("type", "CREW")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("그룹 아이디가 누락되었습니다."));
        }

        @Test
        @DisplayName("실패 (이름 누락)")
        void updateFailTest2() throws Exception {
            //given

            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            //when
            ResultActions perform = mockMvc.perform(
                    put("/api/groups")
                            .contentType(MULTIPART_FORM_DATA)
                            .param("teamId", "1")
                            .param("field", "정보보안")
                            .param("people", "5")
                            .param("repos", "http://github.com")
                            .param("type", "CREW")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            perform.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("그룹 이름이 누락되었습니다."));
        }

    }

    @Nested
    @DisplayName("팀 삭제 테스트")
    @WithMockCustomUser
    class DeleteTeamTest {

        @Test
        @DisplayName("성공")
        void deleteSuccessTest() throws Exception {
            //given

            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            //when
            ResultActions res = mockMvc.perform(
                    delete("/api/groups/1")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("팀 삭제 성공"));
        }

        @Test
        @DisplayName("실패 (query 아이디 누락)")
        void deleteFailTest1() throws Exception {
            //given

            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            //when
            ResultActions res = mockMvc.perform(
                    delete("/api/groups")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isMethodNotAllowed());
        }

        @Test
        @DisplayName("실패 (존재하지 않는 그룹 아이디)")
        void deleteFailTest2() throws Exception {
            //given
            doThrow(new ResourceNotFoundException(NOT_FOUND_CONTENT_ID)).when(teamService).deleteTeam(any(), any());
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            //when
            ResultActions res = mockMvc.perform(
                    delete("/api/groups/1")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value(NOT_FOUND_CONTENT_ID));
        }
    }

    @Nested
    @DisplayName("멤버 추가 테스트")
    @WithMockCustomUser
    class addMemberTest {
        @Test
        @DisplayName("성공")
        void addMemberSuccessTest() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
                    .memberId(12345L)
                    .teamId(1L)
                    .build();
            //when
            ResultActions res = mockMvc.perform(
                    post("/api/groups/member")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("팀 멤버 추가 완료"));
        }

        @Test
        @DisplayName("실패 (그룹 주인과 다른 사용자)")
        void addMemberFailTest1() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            doThrow(new NotAccessUserException(NOT_CORRECT_USER_ID)).when(teamService).addMember(any(), any());
            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
                    .memberId(12345L)
                    .teamId(1L)
                    .build();
            //when
            ResultActions res = mockMvc.perform(
                    post("/api/groups/member")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value(NOT_CORRECT_USER_ID));

        }

        @Test
        @DisplayName("실패 (존재 하지 않는 그룹 아이디)")
        void addMemberFailTest2() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            doThrow(new ResourceNotFoundException(NOT_FOUND_GROUP_ID)).when(teamService).addMember(any(), any());
            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
                    .memberId(12345L)
                    .teamId(1L)
                    .build();
            //when
            ResultActions res = mockMvc.perform(
                    post("/api/groups/member")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value(NOT_FOUND_GROUP_ID));
        }

        @Test
        @DisplayName("실패 (그룹에 존재 하지 않는 사용자 아이디)")
        void addMemberFailTest3() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            doThrow(new ResourceNotFoundException(NOT_FOUND_USER_ID_IN_GROUP)).when(teamService).addMember(any(), any());
            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
                    .memberId(12345L)
                    .teamId(1L)
                    .build();
            //when
            ResultActions res = mockMvc.perform(
                    post("/api/groups/member")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value(NOT_FOUND_USER_ID_IN_GROUP));
        }

        @Test
        @DisplayName("실패 (추가할 사용자 아이디 누락)")
        void addMemberFailTest4() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
                    .teamId(1L)
                    .build();
            //when
            ResultActions res = mockMvc.perform(
                    post("/api/groups/member")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("추가할 사용자 아이디가 누락되었습니다."));
        }

        @Test
        @DisplayName("실패 (추가할 그룹 아이디 누락)")
        void addMemberFailTest5() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
                    .memberId(12345L)
                    .build();
            //when
            ResultActions res = mockMvc.perform(
                    post("/api/groups/member")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.status").value(400))
                    .andExpect(jsonPath("$.message").value("사용자를 추가할 팀 아이디가 누락되었습니다."));
        }
    }

    @Nested
    @DisplayName("멤버 삭제 테스트")
    @WithMockCustomUser
    class DeleteMemberTest {
        @Test
        @DisplayName("성공")
        void deleteMemberSuccessTest() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            TeamAddMemberRequestDto dto = TeamAddMemberRequestDto.builder()
                    .memberId(12345L)
                    .teamId(1L)
                    .build();

            //when
            ResultActions res = mockMvc.perform(
                    delete("/api/groups/member")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(200))
                    .andExpect(jsonPath("$.data").value("팀 멤버 삭제 완료"));
        }

    }
    
    @Nested
    @DisplayName("팀 모집 글 변경 테스트")
    @WithMockCustomUser
    class updateContentTest {
        @Test
        @DisplayName("성공")
        void updateContentSuccessTest() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            TeamContentRequestDto dto = TeamContentRequestDto.builder()
                    .teamId(1L)
                    .content("hello")
                    .build();
            //when
            ResultActions res = mockMvc.perform(
                    put("/api/groups/post")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("팀 모집 정보 변경"));
        }

        @Test
        @DisplayName("실패 (그룹 주인과 사용자가 다른 경우)")
        void updateContentFailTest() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));
            doThrow(new NotAccessUserException(NOT_CORRECT_USER_ID)).when(teamService).updateRecruitingContent(any(), any());
            TeamContentRequestDto dto = TeamContentRequestDto.builder()
                    .teamId(1L)
                    .content("hello")
                    .build();
            //when
            ResultActions res = mockMvc.perform(
                    put("/api/groups/post")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto))
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.status").value(401))
                    .andExpect(jsonPath("$.message").value(NOT_CORRECT_USER_ID));
        }

    }

    @Nested
    @DisplayName("팀원 모집 상태 변경 테스트")
    @WithMockCustomUser
    class changeStatusTest {

        @Test
        @DisplayName("성공")
        void changeStatusSuccessTest() throws Exception {
            //given
            given(userRepository.findById(any())).willReturn(Optional.of(getTestUser()));

            //when
            ResultActions res = mockMvc.perform(
                    get("/api/groups/status/1")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
            );

            //then
            res.andExpect(status().isOk())
                    .andExpect(jsonPath("$.data").value("팀 모집 상태 변경"));
        }

    }

    User getTestUser() {
        User test = User.builder()
                .id(1234L)
                .nickname("test")
                .role(USER)
                .avatarUrl("http://github.com")
                .build();
        return test;
    }

    Team getTestTeam() {
        Team team = Team.builder()
                .id(1111L)
                .name("test")
                .authorId(1234L)
                .field("coding")
                .people(5)
                .repos("http://repos.com")
                .type(Team.TeamType.CREW)
                .mainImage("http://repos.com")
                .build();
        return team;
    }

    UserToTeam getTestRelation() {
        User testUser = getTestUser();
        Team testTeam = getTestTeam();

        UserToTeam build = UserToTeam.builder()
                .user(testUser)
                .team(testTeam)
                .build();

        testTeam.getUsers().add(build);
        testUser.getGroups().add(build);

        return build;
    }


}