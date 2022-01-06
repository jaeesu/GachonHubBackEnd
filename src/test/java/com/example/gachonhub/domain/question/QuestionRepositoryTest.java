package com.example.gachonhub.domain.question;

import com.example.gachonhub.domain.file.File;
import com.example.gachonhub.domain.file.FileRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.google.common.io.ByteStreams;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepository fileRepository;

    @Test
    @DisplayName("존재하지 않는 post id의 조회를 요청 => 에러 발생")
    void notValidPostId() {
        Optional<Question> byId = questionRepository.findById(1L);
        assertThrows(NoSuchElementException.class, () -> {
                byId.orElseThrow(
                        () -> new NoSuchElementException("no such element id is " + 1L));
        });
    }

    @Test
    void saveUser() {
        User user = makeTestUser();
        userRepository.save(user);
//        entityManager.persist(user);
        Assertions.assertThat(userRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("영속성 전이에 따라 question 저장 시 file도 같이 저장")
    void saveQuestionWithFile() throws IOException {
        byte[] bytes = ByteStreams.toByteArray(new FileInputStream((new ClassPathResource("test/testImage.jpeg")).getFile()));
        byte[] bytes2 = ByteStreams.toByteArray(new FileInputStream((new ClassPathResource("test/testImage2.jpeg")).getFile()));
        File file = File.builder().image(bytes).build();
        File file2 = File.builder().image(bytes2).build();

        List<File> list = new ArrayList<>();
        list.add(file);
        list.add(file2);

        User user = makeTestUser();

        user = userRepository.save(user);

        Question question = Question.builder()
                .title("test title")
                .userId(user)
                .fileList(list)
                .build();

        list.stream().forEach(m -> m.setQuestionId(question));

        questionRepository.save(question);

        Assertions.assertThat(questionRepository.findAll().size()).isEqualTo(1);
        Assertions.assertThat(fileRepository.findAll().size()).isEqualTo(2);

    }


    User makeTestUser() {
        return User.builder()
                .id(1234L)
                .build();
    }


}