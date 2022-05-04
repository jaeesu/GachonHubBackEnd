package com.example.gachonhub.redisTemplate.githubRestTemplate;

import com.example.gachonhub.redisTemplate.GithubRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GithubRestTemplateTest {

    @Autowired
    private GithubRestTemplate restTemplate;

    private String orgName = "GachonHub";

//    @Test
//    void getOrgsInfoTest() {
//        ResponseEntity<GithubOrganizationDto> gachonHub = restTemplate.getOrgInfo(orgName);
//        Assertions.assertThat(gachonHub.getBody().getName()).isEqualTo(orgName);
//    }
}