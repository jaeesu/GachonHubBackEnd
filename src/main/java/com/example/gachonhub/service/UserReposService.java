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

    public void updateMainRepository(User user, List<Long> repos) {
        user.getRepos()
                .forEach(x -> x.removeMain());
        if (repos != null && !repos.isEmpty()) reposRepository.updateMainRepository(user, repos);
    }
}
