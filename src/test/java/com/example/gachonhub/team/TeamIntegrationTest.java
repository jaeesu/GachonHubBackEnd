package com.example.gachonhub.team;

import com.example.gachonhub.team.domain.Team;
import com.example.gachonhub.team.domain.TeamRepository;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserToTeam;
import com.example.gachonhub.user.domain.UserToTeamRepository;
import com.example.gachonhub.team.application.TeamService;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import static com.example.gachonhub.user.domain.User.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@SpringBootTest
@DisplayName("team service 통합테스트")
public class TeamIntegrationTest {

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserToTeamRepository userToTeamRepository;

//    @Test
//    @Transactional
//    @DisplayName("team 삭제 통합 테스트 (orphanremoval 동작 확인)")
//    void test() {
//        //given
//        UserToTeam testRelation = getTestRelation();
//
//        //when
//        teamRepository.deleteById(getTestRelation().getTeam().getId());
//
//        //then
//        assertThat(userToTeamRepository.findAll().size()).isEqualTo(0);
//
//    }

    User getTestUser() {
        User test = User.builder()
                .id(1234L)
                .nickname("test")
                .role(USER)
                .avatarUrl("http://github.com")
                .githubToken("token")
                .build();
        entityManager.persist(test);
        return test;
    }

    Team getTestTeam() {
        Team team = Team.builder()
                .name("test")
                .authorId(1234L)
                .field("coding")
                .people(5)
                .repos("http://repos.com")
                .type(Team.TeamType.CREW)
                .avatarUrl("http://repos.com")
                .description("모집 중")
                .build();
        entityManager.persist(team);
        return team;
    }

    UserToTeam getTestRelation() {
        User testUser = getTestUser();
        Team testTeam = getTestTeam();

        UserToTeam build = UserToTeam.builder()
                .user(testUser)
                .team(testTeam)
                .build();

        entityManager.persist(build);
        testTeam.getUsers().add(build);
        testUser.getGroups().add(build);

        entityManager.flush();
        entityManager.clear();

        return build;
    }

}
