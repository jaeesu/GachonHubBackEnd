package com.example.gachonhub.domain.team;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.domain.user.relation.UserToTeam;
import com.example.gachonhub.domain.user.relation.UserToTeamRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import static com.example.gachonhub.domain.user.User.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("prod")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UserToTeamRepository userToTeamRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;


    @Test
    @DisplayName("team만 삭제했을 때 orpahnremove가 되지 않는다.")
    void removeTeamTest1(){
        //given
        UserToTeam relation = getTestRelation();

        //when
        teamRepository.deleteById(relation.getTeam().getId());

        //then
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userToTeamRepository.findAll());
    }

    @Test
    @DisplayName("user만 삭제했을 때 orpahnremove가 되지 않는다.")
    void removeTeamTest2(){
        //given
        UserToTeam relation = getTestRelation();

        //when
        userRepository.deleteById(relation.getUser().getId());

        //then
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> userToTeamRepository.findAll());
    }

    @Test
    @DisplayName("relation 저장 시, user, team에서도 업데이트를 해야한다.")
    void removeTeamTest3(){
        //given
        UserToTeam relation = getTestRelation();

        //when
        User user = userRepository.getById(relation.getUser().getId());
        Team team = teamRepository.getById(relation.getTeam().getId());

        //then
        assertThat(user.getGroups().size()).isEqualTo(1);
        assertThat(team.getUsers().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("relation 삭제 시, orphanremove를 이용")
    void removeTeamTest4(){
        //given
        UserToTeam testRelation = userToTeamRepository.getById(getTestRelation().getId());
        User user = testRelation.getUser();
        Team team = testRelation.getTeam();

        //when
        user.getGroups().remove(testRelation);
        team.getUsers().remove(testRelation);

        //then
        assertThat(userRepository.getById(user.getId()).getGroups().size()).isEqualTo(0);
        assertThat(teamRepository.getById(team.getId()).getUsers().size()).isEqualTo(0);
        assertThat(userToTeamRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("user에 team 추가 테스트")
    void addTeamTest(){
        //given
        User testUser = getTestUser();
        Team testTeam = getTestTeam();
        UserToTeam relation = UserToTeam.builder()
                .team(testTeam)
                .user(testUser)
                .build();

        //when
        userToTeamRepository.save(relation);
        testUser.getGroups().add(relation);
        testTeam.getUsers().add(relation);

        //then
        UserToTeam byId = userToTeamRepository.getById(relation.getId());
        assertThat(userRepository.getById(testUser.getId()).getGroups().size()).isEqualTo(1);
        assertThat(teamRepository.getById(testTeam.getId()).getUsers().size()).isEqualTo(1);
        assertThat(userToTeamRepository.findAll().size()).isEqualTo(1);
        assertThat(byId.getTeam()).isEqualTo(testTeam);
        assertThat(byId.getUser()).isEqualTo(testUser);
    }

    User getTestUser() {
        User test = User.builder()
                .id(1234L)
                .nickname("test")
                .role(USER)
                .avatarUrl("http://github.com")
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
                .mainImage("http://repos.com")
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

        userToTeamRepository.save(build);
        testTeam.getUsers().add(build);
        testUser.getGroups().add(build);

        entityManager.flush();
        entityManager.clear();

        return build;
    }



}