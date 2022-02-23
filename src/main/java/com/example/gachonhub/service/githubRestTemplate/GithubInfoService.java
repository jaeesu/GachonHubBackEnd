package com.example.gachonhub.service.githubRestTemplate;

import com.example.gachonhub.domain.commitInfo.CommitInfo;
import com.example.gachonhub.domain.commitInfo.CommitInfoRepository;
import com.example.gachonhub.domain.commitInfo.dto.CommitInfoDto;
import com.example.gachonhub.domain.commitInfo.dto.GithubRepositoryDto;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.userInfo.UserRepos;
import com.example.gachonhub.domain.user.userInfo.UserReposRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GithubInfoService {

    private final UserReposRepository reposRepository;
    private final GithubRestTemplate githubRestTemplate;
    private final CommitInfoRepository commitInfoRepository;

    public void saveUserCommitInfo(User user) {
        List<UserRepos> userRepos = saveGithubUserRepositories(user);
        userRepos.stream()
                        .forEach(x -> saveGithubRepositoryCommits(user, x));
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
}
