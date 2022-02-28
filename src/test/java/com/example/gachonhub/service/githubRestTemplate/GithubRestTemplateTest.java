package com.example.gachonhub.service.githubRestTemplate;

import com.example.gachonhub.domain.commitInfo.dto.GithubOrganizationDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class GithubRestTemplateTest {

    @Autowired
    private GithubRestTemplate restTemplate;

    private String orgName = "GachonHub";

    @Test
    void getOrgsInfoTest() {
        ResponseEntity<GithubOrganizationDto> gachonHub = restTemplate.getOrgInfo(orgName);
        Assertions.assertThat(gachonHub.getBody().getName()).isEqualTo(orgName);
    }
}