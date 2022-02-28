package com.example.gachonhub.service.githubRestTemplate;

import com.example.gachonhub.domain.commitInfo.CommitInfo;
import com.example.gachonhub.domain.commitInfo.CommitInfoRepository;
import com.example.gachonhub.domain.commitInfo.dto.CommitInfoDto;
import com.example.gachonhub.domain.commitInfo.dto.GithubRepositoryDto;
import com.example.gachonhub.domain.team.Team;
import com.example.gachonhub.domain.team.TeamRepository;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.UserRepository;
import com.example.gachonhub.domain.user.userInfo.GithubRepos;
import com.example.gachonhub.domain.user.userInfo.GithubReposRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.util.ErrorUtil;
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

    private final GithubReposRepository reposRepository;
    private final GithubRestTemplate githubRestTemplate;
    private final CommitInfoRepository commitInfoRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    public void saveUserCommitInfo(User user) {
        List<GithubRepos> githubRepos = saveGithubUserRepositories(user);
        githubRepos.stream()
                        .forEach(x -> saveGithubRepositoryCommits(user, x));
        user.setCommitCount(commitInfoRepository.countAllByUserId_Id(user.getId()));

    }

    public void saveOrgCommitInfo(User user, Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException(ErrorUtil.NOT_FOUND_GROUP_ID));
        List<GithubRepos> githubRepos = saveOrganizationRepositories(team);
        githubRepos.stream()
                .forEach(x -> saveOrganizationRepositoryCommits(user, team, x));
    }


    public List<GithubRepos> saveGithubUserRepositories(User user) {
        List<GithubRepositoryDto> userGithubRepositories = githubRestTemplate.getUserGithubRepositories(user);
        Set<GithubRepos> reposSet = userGithubRepositories.stream()
                .map(x -> x.toEntity(user))
                .collect(Collectors.toSet());
        return reposRepository.saveAll(reposSet);
    }

    public void saveGithubRepositoryCommits(User user, GithubRepos githubRepos) {
        List<CommitInfoDto> repositoryCommit = githubRestTemplate.getRepositoryCommitByUser(user, githubRepos.getFullName());
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

    public List<GithubRepos> saveOrganizationRepositories(Team team) {
        List<GithubRepositoryDto> orgRepos = githubRestTemplate.getOrgRepos(team.getRepos());
        Set<GithubRepos> reposSet = orgRepos.stream()
                .map(x -> x.toEntity(team))
                .collect(Collectors.toSet());
        return reposRepository.saveAll(reposSet);
    }

    public void saveOrganizationRepositoryCommits(User user, Team team, GithubRepos githubRepos) {
        List<CommitInfoDto> repositoryCommit = githubRestTemplate.getRepositoryCommit(user, githubRepos.getFullName());
        List<CommitInfo> commitInfoList = repositoryCommit.stream()
                .map(x -> x.toEntity(team))
                .collect(Collectors.toList());

        commitInfoRepository.saveAll(commitInfoList);

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
