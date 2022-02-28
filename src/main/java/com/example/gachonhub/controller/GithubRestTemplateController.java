package com.example.gachonhub.controller;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import com.example.gachonhub.service.githubRestTemplate.GithubInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.example.gachonhub.payload.response.ResponseUtil.success;
import static com.example.gachonhub.util.ErrorUtil.NOT_FOUND_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GithubRestTemplateController {

    private final UserRepository userRepository;
    private final GithubInfoService githubInfoService;

    @GetMapping("/repos")
    public ResponseEntity<?> getGithubRepos(@CurrentUser UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        githubInfoService.saveUserCommitInfo(user);
        return success("개인 프로필 깃허브 레포지토리, 커밋 정보 연동 완료");
    }

    @GetMapping("/rank/personal")
    public ResponseEntity<?> getPersonalCommitRank() {
        return success(githubInfoService.getUserCommitRank());
    }

    @GetMapping("/rank/groups")
    public ResponseEntity<?> getGroupsCommitRank() {
        return success(githubInfoService.getGroupoCommitRank());
    }


    @GetMapping("/team/repos")
    public ResponseEntity<?> getOrganizationRepos(@CurrentUser UserPrincipal userPrincipal, @RequestParam("team") Long teamId) {
        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException(NOT_FOUND_USER_ID));
        githubInfoService.saveOrgCommitInfo(user, teamId);
        return success("그룹 프로필 깃허브 레포지토리, 커밋 정보 연동 완료");
    }

}
