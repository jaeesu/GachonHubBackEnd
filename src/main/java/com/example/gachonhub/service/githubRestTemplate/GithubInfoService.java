package com.example.gachonhub.service.githubRestTemplate;

import com.example.gachonhub.domain.commitInfo.CommitInfo;
import com.example.gachonhub.domain.commitInfo.CommitInfoRepository;
import com.example.gachonhub.domain.commitInfo.dto.CommitInfoDto;
import com.example.gachonhub.domain.commitInfo.dto.GithubRepositoryDto;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.domain.user.userInfo.UserRepos;
import com.example.gachonhub.domain.user.userInfo.UserReposRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GithubInfoService {

    private final UserReposRepository reposRepository;
    private final GithubRestTemplate githubRestTemplate;
    private final CommitInfoRepository commitInfoRepository;
    private final UserRepository userRepository;

    public void saveUserCommitInfo(User user) {
        List<UserRepos> userRepos = saveGithubUserRepositories(user);
        userRepos.stream()
                        .forEach(x -> saveGithubRepositoryCommits(user, x));
        user.setCommitCount(commitInfoRepository.countAllByUserId_Id(user.getId()));

    }

    public List<UserRepos> saveGithubUserRepositories(User user) {
        List<GithubRepositoryDto> userGithubRepositories = githubRestTemplate.getUserGithubRepositories(user);
        Set<UserRepos> reposSet = userGithubRepositories.stream()
                .map(x -> x.toEntity(user))
                .collect(Collectors.toSet());
        return reposRepository.saveAll(reposSet);
    }

    public void saveGithubRepositoryCommits(User user, UserRepos userRepos) {
        List<CommitInfoDto> repositoryCommit = githubRestTemplate.getRepositoryCommit(user, userRepos.getFullName());
        Set<CommitInfo> commitInfoSet = repositoryCommit.stream()
                .map(x -> x.toEntity(user))
                .collect(Collectors.toSet());
        commitInfoRepository.saveAll(commitInfoSet);
    }

    public List<commitInfoUser> getUserCommitRank() {
        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "commitCount"))
                .stream().map(x -> new commitInfoUser(x.getId(), x.getNickname(), x.getCommitCount(), 999L))
                .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    class commitInfoUser {
        Long id;
        String nickname;
        Long commit = 0L;
        Long lastCommit;
    }
}
