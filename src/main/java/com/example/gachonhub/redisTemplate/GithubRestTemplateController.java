package com.example.gachonhub.redisTemplate;

import com.example.gachonhub.commitInfo.dto.CommitCountDto;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.security.CurrentUser;
import com.example.gachonhub.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.example.gachonhub.common.ui.out.ResponseUtil.success;
import static com.example.gachonhub.common.exception.ErrorUtil.NOT_FOUND_USER_ID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/github")
public class GithubRestTemplateController {

    private final UserRepository userRepository;
    private final GithubInfoService githubInfoService;

    @GetMapping("/commitTable")
    public ResponseEntity<?> getGithubCommitTable(@RequestParam("userId") Long id) {
        List<CommitCountDto> commitTable = githubInfoService.getCommitTable(id);
        return success(commitTable);
    }

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
