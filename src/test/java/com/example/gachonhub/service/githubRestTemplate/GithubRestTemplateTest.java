package com.example.gachonhub.service.githubRestTemplate;

import com.example.gachonhub.domain.commitInfo.dto.GithubRepositoryDto;
import com.example.gachonhub.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static com.example.gachonhub.domain.user.User.Role.USER;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class GithubRestTemplateTest {

    @Autowired
    private GithubRestTemplate githubRestTemplate;

    @Test
    void test() {
        User testUser = getTestUser();
        List<GithubRepositoryDto> userGithubRepositories = githubRestTemplate.getUserGithubRepositories(testUser);

        for (GithubRepositoryDto d : userGithubRepositories) {
            System.out.println(d);
        }
    }

    User getTestUser() {
        User test = User.builder()
                .id(1234L)
                .nickname("jaeesu")
                .role(USER)
                .avatarUrl("http://github.com")
                .build();
        return test;
    }

}