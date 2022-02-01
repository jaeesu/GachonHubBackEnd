package com.example.gachonhub.domain.question;

import com.example.gachonhub.domain.category.MainCategory;
import com.example.gachonhub.domain.category.SubCategory;
import com.example.gachonhub.domain.file.FileRepository;
import com.example.gachonhub.domain.file.UserFile;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.request.QuestionRequestDto;
import com.google.common.io.ByteStreams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("prod")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //무슨 차이...?
@DataJpaTest
@DisplayName("질문글 레포지토리 테스트")
class PostQuestionRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private FileRepository fileRepository;

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
        assertThat(fileRepository.findAll().size()).isEqualTo(2);
        List<UserFile> list1 = new ArrayList<>(questionRepository.findAll().get(0).getUserFileList());
        assertThat(list1.get(0).getPostQuestionId()).isNotEqualTo(null);
        assertThat(list1.get(1).getPostQuestionId()).isNotEqualTo(null);
    }

    /**
     * 삭제 후 저장하는 방식으로 다시 수정할 것,
     * 또한 이미지를 blob 형식으로 저장하지 않고 s3에 저장하여 url을 프론트에 제공할 것
     */
//    @Test
//    @Rollback(false)
//    @DisplayName("질문글 수정 시, orphanremoval에 따라 파일도 수정된다.")
//    void questionRelationTest() throws IOException {
//        //Caused by: java.sql.SQLSyntaxErrorException: Table 'gachonhub.post_question_user_file_list' doesn't exist
//        //given
//        PostQuestion postQuestion = getTestQuestion();
//        postQuestion.getUserFileList().forEach(m -> m.updateQuestion(postQuestion));
//        questionRepository.save(postQuestion);
//
//        //when
//        byte[] bytes = ByteStreams.toByteArray(new FileInputStream((new ClassPathResource("test/testImage3.jpeg")).getFile()));
//        UserFile userFile = UserFile.builder()
//                .image(bytes)
//                .build();
//        List<UserFile> userFiles = new ArrayList<>();
//        userFiles.add(userFile);
//
//        //새로 객체를 만드는 경우 orphanremoval 됨
//        PostQuestion postQuestion2 = PostQuestion.builder()
//                .id(postQuestion.getId())
//                .userId(postQuestion.getUserId())
//                .title("test title2")
//                .content("test content2")
//                .userFileList(userFiles)
//                .categoryId(postQuestion.getCategoryId())
//                .build();
//
//        userFiles.forEach(m -> m.updateQuestion(postQuestion2));
//        questionRepository.save(postQuestion2);
//
//        //then
//        List<PostQuestion> list = questionRepository.findAll();
//        PostQuestion postQuestion3 = questionRepository.getById(postQuestion.getId());
//
//        assertThat(list.size()).isEqualTo(1);
//        assertThat(postQuestion2.getId()).isEqualTo(postQuestion3.getId());
//        assertThat(postQuestion2.getUserId().getId()).isEqualTo(1234L);
//        assertThat(postQuestion2.getTitle()).isEqualTo("test title2");
//        assertThat(postQuestion2.getContent()).isEqualTo("test content2");
//        assertThat(postQuestion2.getCategoryId().getName()).isEqualTo("test2");
//        assertThat(postQuestion2.getUserFileList().size()).isEqualTo(1);
//        assertThat(fileRepository.findByPostQuestionId_Id(postQuestion2.getId()).size()).isEqualTo(1);
//
//    }
//
//    @Test
////    @Rollback(false)
//    @DisplayName("질문글 수정 시, 새로 객체를 만들지 않으면 orphanremoval이 제대로 동작하지 않는다.")
//    void questionRelationTest1() throws IOException {
//        //given
//        PostQuestion postQuestion = getTestQuestion();
//        postQuestion.getUserFileList().forEach(m -> m.updateQuestion(postQuestion));
//        questionRepository.save(postQuestion);
//
//        //when
//        byte[] bytes = ByteStreams.toByteArray(new FileInputStream((new ClassPathResource("test/testImage3.jpeg")).getFile()));
//        UserFile userFile = UserFile.builder().image(bytes).build();
//        List<UserFile> userFiles = new ArrayList<>();
//        userFiles.add(userFile);
//
//        QuestionRequestDto dto = QuestionRequestDto.builder()
//                .id(postQuestion.getId())
//                .title("test title2")
//                .content("test content2")
//                .category(postQuestion.getCategoryId().getId())
//                .build();
//
////        새로 객체를 만들지 않는 경우 orphanremoval 안됨
//        PostQuestion postQuestion1 = dto.toEntity(postQuestion.getUserId(), postQuestion.getCategoryId(), null);
//        userFiles.stream().forEach(m -> m.updateQuestion(postQuestion1));
//        questionRepository.save(postQuestion1);
//
//        //then
//        List<PostQuestion> list = questionRepository.findAll();
//        PostQuestion postQuestion2 = questionRepository.getById(postQuestion.getId());
//
//        assertThat(list.size()).isEqualTo(1);
//        assertThat(postQuestion2.getId()).isEqualTo(postQuestion.getId());
//        assertThat(postQuestion2.getUserId().getId()).isEqualTo(postQuestion.getUserId().getId());
//        assertThat(postQuestion2.getTitle()).isEqualTo(postQuestion.getTitle());
//        assertThat(postQuestion2.getContent()).isEqualTo(postQuestion.getContent());
//        assertThat(postQuestion2.getCategoryId().getId()).isEqualTo(postQuestion.getCategoryId().getId());
//        assertThat(postQuestion2.getUserFileList().size()).isEqualTo(1);
//        assertThat(fileRepository.findByPostQuestionId_Id(postQuestion.getId()).size()).isEqualTo(3);
//
//    }

    User getTestUser() {
        User user = User.builder()
                .id(1234L)
                .nickname("test")
                .role(User.Role.USER)
                .avatar_url("http://github.com")
                .build();
        entityManager.persist(user);
        return user;
    }

    List<UserFile> getTestFileList() throws IOException {
        byte[] bytes = ByteStreams.toByteArray(new FileInputStream((new ClassPathResource("test/testImage.jpeg")).getFile()));
        byte[] bytes2 = ByteStreams.toByteArray(new FileInputStream((new ClassPathResource("test/testImage2.jpeg")).getFile()));
        UserFile userFile = UserFile.builder().image(bytes).build();
        UserFile userFile2 = UserFile.builder().image(bytes2).build();

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