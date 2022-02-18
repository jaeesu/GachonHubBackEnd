package com.example.gachonhub.service;

import com.example.gachonhub.domain.user.User;
import com.example.gachonhub.domain.user.userInfo.UserReposRepository;
import com.example.gachonhub.payload.request.ReposRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReposService {

    private final UserReposRepository reposRepository;

    public void updateMainRepository(User user, ReposRequestDto repos) {
        user.getRepos()
                .forEach(x -> x.removeMain());
        reposRepository.updateMainRepository(user, repos.getRepos());
    }
}
