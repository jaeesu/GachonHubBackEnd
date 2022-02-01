package com.example.gachonhub.service;

import com.example.gachonhub.domain.category.MainCategory;
import com.example.gachonhub.domain.category.SubCategory;
import com.example.gachonhub.domain.file.UserFile;
import com.example.gachonhub.domain.file.FileRepository;
import com.example.gachonhub.domain.question.PostQuestion;
import com.example.gachonhub.domain.question.QuestionRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.payload.request.QuestionRequestDto;
import com.google.common.io.ByteStreams;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("prod")
@DisplayName("질문글 서비스 테스트")
class PostQuestionServiceTest {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private EntityManager entityManager;

//    @Nested
//    @DisplayName("질문글 수정 테스트")
//    @Rollback(false)
//    class PostQuestionUpdateTest {
//
//        @Test
//        @DisplayName("성공")
//        void questionUpdateTest1() throws IOException, IllegalAccessException {
//            //given
//
//            //when
//
//            //then
//        }
//
//    }



    QuestionRequestDto getQuestionRequestDto(Long id) throws IOException {
        List<MultipartFile> testMultiPart = getTestMultiPart();


        return QuestionRequestDto.builder()
                .id(id)
                .title("test title2")
                .category(getTestCategory().getId())
                .content("test content2")
                .files(testMultiPart)
                .build();
    }

    User getTestUser() {
        User build = User.builder()
                .id(1234L)
                .build();
        entityManager.persist(build);
        return build;
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

    List<MultipartFile> getTestMultiPart() throws IOException {
        MockMultipartFile file = new MockMultipartFile("testImage",
                new FileInputStream((new ClassPathResource("test/testImage.jpeg")).getFile()));
        MockMultipartFile file2 = new MockMultipartFile("testImage2",
                new FileInputStream((new ClassPathResource("test/testImage2.jpeg")).getFile()));

        List<MultipartFile> fileList = new ArrayList<>();
        fileList.add(file);
        fileList.add(file2);

        return fileList;
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


    PostQuestion getTestQuestion(User user, List<UserFile> userFileList) {

        return PostQuestion.builder()
                .title("test title")
                .userId(user)
                .content("test content")
                .categoryId(getTestCategory())
                .userFileList(userFileList)
                .build();
    }

}