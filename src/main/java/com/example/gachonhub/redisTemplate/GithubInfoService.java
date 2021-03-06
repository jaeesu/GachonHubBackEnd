package com.example.gachonhub.redisTemplate;

import com.example.gachonhub.commitInfo.domain.CommitInfo;
import com.example.gachonhub.commitInfo.domain.CommitInfoRepository;
import com.example.gachonhub.commitInfo.dto.CommitCountDto;
import com.example.gachonhub.commitInfo.dto.CommitInfoDto;
import com.example.gachonhub.commitInfo.dto.GithubRepositoryDto;
import com.example.gachonhub.team.domain.Team;
import com.example.gachonhub.team.domain.TeamRepository;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.UserRepository;
import com.example.gachonhub.user.domain.userInfo.GithubRepos;
import com.example.gachonhub.user.domain.userInfo.GithubReposRepository;
import com.example.gachonhub.exception.ResourceNotFoundException;
import com.example.gachonhub.common.exception.ErrorUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
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
    private LocalDateTime lastDate = LocalDate.now().minusDays(1L).atStartOfDay();

    public List<CommitCountDto> getCommitTable(Long userId) {
        List<CommitCountDto> commitCountPerDayById = commitInfoRepository.findCommitCountPerDayById(userId);
        Collections.sort(commitCountPerDayById, (o1, o2) -> (o1.getDate().compareTo(o2.getDate())));
        return commitCountPerDayById;
    }

    public void saveUserCommitInfo(User user) {
        List<GithubRepos> githubRepos = saveGithubUserRepositories(user);
        githubRepos.stream()
                        .forEach(x -> saveGithubRepositoryCommits(user, x));

        user.setCommitCount(commitInfoRepository.countAllByUserId_Id(user.getId()));


    }

    public void saveOrgCommitInfo(User user, Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() -> new ResourceNotFoundException(ErrorUtil.NOT_FOUND_GROUP_ID));
        List<GithubRepos> githubRepos = saveOrganizationRepositories(user, team);
        githubRepos.stream()
                .forEach(x -> saveOrganizationRepositoryCommits(user, team, x));
        team.setCommitCount(commitInfoRepository.countAllByTeamId_Id(team.getId()));
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

    public List<GithubRepos> saveOrganizationRepositories(User user, Team team) {
        List<GithubRepositoryDto> orgRepos = githubRestTemplate.getOrgRepos(user, team.getRepos());
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

    public List<UserCommitInfoDto> getUserCommitRank() {

        return userRepository.findAll(Sort.by(Sort.Direction.DESC, "commitCount"))
                .stream().map(x -> new UserCommitInfoDto(x.getId(), x.getNickname(),
                        commitInfoRepository.countAllByUserId_IdAndDateAfter(x.getId(), lastDate) ,x.getCommitCount()))
                .collect(Collectors.toList());
    }

    public List<GroupCommitInfoDto> getGroupoCommitRank() {
        return teamRepository.findAll(Sort.by(Sort.Direction.DESC, "commitCount"))
                .stream().map(x -> new GroupCommitInfoDto(x.getId(), x.getName(),
                        commitInfoRepository.countAllByTeamId_IdAndDateAfter(x.getId(), lastDate), x.getCommitCount()))
                .collect(Collectors.toList());
    }

    @Getter
    @AllArgsConstructor
    class UserCommitInfoDto {
        Long id;
        String name;
        Long lastCommit;
        Long commit;
    }

    @Getter
    @AllArgsConstructor
    class GroupCommitInfoDto {
        Long id;
        String name;
        Long lastCommit;
        Long commit;
    }

}
