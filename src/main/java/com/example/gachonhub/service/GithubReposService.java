package com.example.gachonhub.service;

import com.example.gachonhub.domain.team.Team;
import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.userInfo.GithubReposRepository;
import com.example.gachonhub.exception.NotAccessUserException;
import com.example.gachonhub.payload.request.TeamRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.gachonhub.util.ErrorUtil.NOT_CORRECT_USER_ID;

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
