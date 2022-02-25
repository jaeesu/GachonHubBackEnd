package com.example.gachonhub.controller;

import com.example.gachonhub.domain.commitInfo.CommitInfoRepository;
import com.example.gachonhub.domain.commitInfo.dto.CommitInfoDto;
import com.example.gachonhub.domain.commitInfo.dto.GithubRepositoryDto;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.payload.response.ResponseUtil;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.service.githubRestTemplate.GithubInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.gachonhub.payload.response.ResponseUtil.success;
import static com.example.gachonhub.util.ErrorUtil.NOT_FOUND_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GithubRestTemplateController {

    private final UserRepository userRepository;
    private final GithubInfoService githubInfoService;
    private final CommitInfoRepository commitInfoRepository;

    @GetMapping("/repos")
    public ResponseEntity<?> getGithubRepos(@CurrentUser UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        githubInfoService.saveUserCommitInfo(user);;
        return success("깃허브 레포지토리, 커밋 정보 연동 완료");
    }

    @GetMapping("/rank")
    public ResponseEntity<?> getCommitRank() {
        return success(githubInfoService.getUserCommitRank());
    }

}
