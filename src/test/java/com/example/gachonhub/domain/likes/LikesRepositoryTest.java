package com.example.gachonhub.domain.likes;

import com.example.gachonhub.domain.comment.Comment;
import com.example.gachonhub.domain.contest.PostContest;
import com.example.gachonhub.domain.question.PostQuestion;
import com.example.gachonhub.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import static com.example.gachonhub.domain.user.User.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@DisplayName("공감 레포지토리 테스트")
class LikesRepositoryTest {

    @Autowired
    private LikesRepository likesRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("사용자 아이디와 공모전 아이디로 공감 삭제하기")
    void findByUserAndContest(){
        //given
        PostContest contest = PostContest.builder().title("title").content("content").build();
        entityManager.persist(contest);
        User testUser = getTestUser();
        Likes likes = Likes.builder().postContest(contest).user(testUser).build();
        entityManager.persist(likes);

        //when
        likesRepository.deleteByUser_IdAndPostContest_Id(testUser.getId(), contest.getId());

        //then
        assertThat(likesRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("사용자 아이디와 상위 댓글 아이디로 삭제하기")
    void findByUserAndParentComment(){
        //given
        Comment comment = Comment.builder().content("good").build();
        entityManager.persist(comment);
        User testUser = getTestUser();
        Likes likes = Likes.builder().parentComment(comment).user(testUser).build();
        entityManager.persist(likes);

        //when
        likesRepository.deleteByUser_IdAndParentComment_Id(testUser.getId(), comment.getId());

        //then
        assertThat(likesRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("사용자 아이디와 질문글 아이디로 삭제하기")
    void findByUserAndQuestion(){
        //given
        PostQuestion question = PostQuestion.builder().title("title").build();
        entityManager.persist(question);
        User testUser = getTestUser();
        Likes likes = Likes.builder().postQuestion(question).user(testUser).build();
        entityManager.persist(likes);

        //when
        likesRepository.deleteByUser_IdAndPostQuestion_Id(testUser.getId(), question.getId());

        //then
        assertThat(likesRepository.findAll().size()).isEqualTo(0);
    }

    User getTestUser() {
        User test = User.builder().id(1234L).nickname("test").role(USER).build();
        entityManager.persist(test);
        return test;
    }

}