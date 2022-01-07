package com.example.gachonhub.domain.question;

import com.example.gachonhub.domain.file.File;
import com.example.gachonhub.domain.file.FileRepository;
import com.example.gachonhub.domain.user.User;
import com.google.common.io.ByteStreams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class QuestionRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

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

        Question question = Question.builder()
                .title("test title")
                .userId(user)
                .fileList(list)
                .build();

        list.stream().forEach(m -> m.setQuestionId(question));

        questionRepository.save(question);

        assertThat(questionRepository.findAll().size()).isEqualTo(1);
        assertThat(fileRepository.findAll().size()).isEqualTo(2);
        assertThat(questionRepository.findAll().get(0).getFileList().get(0).getQuestionId()).isNotEqualTo(null);
        assertThat(questionRepository.findAll().get(0).getFileList().get(1).getQuestionId()).isNotEqualTo(null);

    }


    User makeTestUser() {
        User user = User.builder()
                .id(1234L)
                .build();
        entityManager.persist(user);
        return user;
    }


}