package com.example.gachonhub.question.domain;

import com.example.gachonhub.category.domain.MainCategory;
import com.example.gachonhub.category.domain.SubCategory;
import com.example.gachonhub.file.domain.UserFile;
import com.example.gachonhub.file.domain.UserFileRepository;
import com.example.gachonhub.question.domain.PostQuestion;
import com.example.gachonhub.question.domain.QuestionRepository;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.test.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("local")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //무슨 차이...?
@DataJpaTest
@Import(TestConfig.class)
@DisplayName("질문글 레포지토리 테스트")
class PostQuestionRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserFileRepository userFileRepository;

    @Test
    @DisplayName("존재하지 않는 post id의 조회를 요청 => 에러 발생")
    void notValidPostId() {
        Optional<PostQuestion> byId = questionRepository.findById(1L);
        assertThrows(ResourceNotFoundException.class, () -> {
                byId.orElseThrow(
                        () -> new ResourceNotFoundException("no such element id is " + 1L));
        });
    }

    @Test
    @DisplayName("영속성 전이에 따라 question 저장 시 file도 같이 저장")
    void saveQuestionWithFile() throws IOException {

        PostQuestion postQuestion = getTestQuestion();
        postQuestion.getUserFileList().forEach(m -> m.updateQuestion(postQuestion));

        questionRepository.save(postQuestion);

        assertThat(questionRepository.findAll().size()).isEqualTo(1);
        assertThat(userFileRepository.findAll().size()).isEqualTo(2);
        List<UserFile> list1 = new ArrayList<>(questionRepository.findAll().get(0).getUserFileList());
        assertThat(list1.get(0).getPostQuestionId()).isNotEqualTo(null);
        assertThat(list1.get(1).getPostQuestionId()).isNotEqualTo(null);
    }

    User getTestUser() {
        User user = User.builder()
                .id(1234L)
                .nickname("test")
                .role(User.Role.USER)
                .avatarUrl("http://github.com")
                .build();
        entityManager.persist(user);
        return user;
    }

    List<UserFile> getTestFileList() throws IOException {

        String url1 = "https://spring.io/images/spring-logo-9146a4d3298760c2e7e49595184e1975.svg";
        String url2 = "https://spring.io/images/spring-initializr-4291cc0115eb104348717b82161a81de.svg";

        UserFile userFile = UserFile.builder()
                .imageUrl(url1)
                .realName("test1.svg")
                .build();
        UserFile userFile2 = UserFile.builder()
                .imageUrl(url2)
                .realName("test2.svg")
                .build();

        List<UserFile> list = new ArrayList<>();
        list.add(userFile);
        list.add(userFile2);

        return list;
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

    PostQuestion getTestQuestion() throws IOException {
        User testUser = getTestUser();
        List<UserFile> testFileList = getTestFileList();
        SubCategory testCategory = getTestCategory();

        return PostQuestion.builder()
                .title("test title")
                .userId(testUser)
                .content("test content")
                .categoryId(testCategory)
                .userFileList(testFileList)
                .build();
    }

}