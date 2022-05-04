package com.example.gachonhub.redisTemplate.githubRestTemplate;

import com.example.gachonhub.redisTemplate.GithubInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GithubInfoServiceTest {

    @Autowired
    GithubInfoService githubInfoService;

    @Test
    @DisplayName("커밋 정보 추출 반환값 확인 테스트")
    public void test() {
        githubInfoService.getCommitTable(50989437L);
    }
}