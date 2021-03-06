package com.example.gachonhub.likes.domain;

import com.example.gachonhub.category.domain.MainCategory;
import com.example.gachonhub.category.domain.SubCategory;
import com.example.gachonhub.comment.domain.Comment;
import com.example.gachonhub.contest.domain.PostContest;
import com.example.gachonhub.likes.domain.Likes;
import com.example.gachonhub.likes.domain.LikesRepository;
import com.example.gachonhub.question.domain.PostQuestion;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.test.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import static com.example.gachonhub.user.domain.User.Role.USER;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@Import(TestConfig.class)
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
        MainCategory main = MainCategory.builder().name("first").build();
        entityManager.persist(main);
        SubCategory second = SubCategory.builder().mainCategory(main).name("second").build();
        entityManager.persist(second);
        User testUser1 = getTestUser();
        PostContest contest = PostContest.builder()
                .user(testUser1)
                .title("title")
                .content("content")
                .categoryId(second)
                .build();
        entityManager.persist(contest);
        User testUser2 = getTestUser2();
        Likes likes = Likes.builder().postContest(contest).user(testUser2).build();
        entityManager.persist(likes);

        //when
        likesRepository.deleteByUser_IdAndPostContest_Id(testUser2.getId(), contest.getId());

        //then
        assertThat(likesRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("사용자 아이디와 상위 댓글 아이디로 삭제하기")
    void findByUserAndParentComment(){
        //given
        User testUser2 = getTestUser2();

        Comment comment = Comment.builder().content("good").userId(testUser2).build();
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
        SubCategory testCategory = getTestCategory();
        User testUser1 = getTestUser();

        PostQuestion question = PostQuestion.builder().title("title").categoryId(testCategory).content("content").userId(testUser1).build();
        entityManager.persist(question);
        User testUser2 = getTestUser2();
        Likes likes = Likes.builder().postQuestionId(question).user(testUser2).build();
        entityManager.persist(likes);

        //when
        likesRepository.deleteByUser_IdAndPostQuestionId_Id(testUser2.getId(), question.getId());

        //then
        assertThat(likesRepository.findAll().size()).isEqualTo(0);
    }

    User getTestUser() {
        User test = User.builder().id(1234L).nickname("test").role(USER).avatarUrl("http://github.com").build();
        entityManager.persist(test);
        return test;
    }

    User getTestUser2() {
        User test = User.builder().id(12345L).nickname("test2").role(USER).avatarUrl("http://github.com").build();
        entityManager.persist(test);
        return test;
    }

    SubCategory getTestCategory() {
        MainCategory test1 = MainCategory.builder()
                .name("test1").build();
        entityManager.persist(test1);
        SubCategory test2 = SubCategory.builder()
                .mainCategory(test1).name("test2").build();
        entityManager.persist(test2);
        return test2;
    }


}