package com.example.gachonhub.service;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.userInfo.UserRepos;
import com.example.gachonhub.domain.user.userInfo.UserReposRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReposService {

    private final UserReposRepository reposRepository;

    public void updateMainRepository(User user, List<Integer> repos) {
        List<UserRepos> reposList = reposRepository.findAllByUser_IdAndMainIsTrue(user.getId());
        reposList.stream()
                .forEach(x -> x.removeMain());
        List<UserRepos> repos1 = reposRepository.findAllByIds(repos);
        repos1.stream()
                .forEach(x -> x.addMain());
    }
}
