package com.example.gachonhub.redisTemplate;

import com.example.gachonhub.team.domain.Team;
import com.example.gachonhub.user.domain.User;
import com.example.gachonhub.user.domain.userInfo.GithubReposRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GithubReposService {

    private final GithubReposRepository reposRepository;

    public void updateUserMainRepository(User user, List<Long> repos) {
        user.getRepos()
                .forEach(x -> x.removeMain());
        if (repos != null && !repos.isEmpty()) reposRepository.updateMainRepository(repos);
    }
    public void updateGroupMainRepository(Team team, List<Long> repos) {
        team.getReposList()
                .forEach(x -> x.removeMain());
        if (repos != null && !repos.isEmpty()) reposRepository.updateMainRepository(repos);
    }

}
