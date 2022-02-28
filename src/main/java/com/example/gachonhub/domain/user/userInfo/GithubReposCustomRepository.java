package com.example.gachonhub.domain.user.userInfo;

import com.example.gachonhub.domain.user.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GithubReposCustomRepository {

    void updateMainRepository(List<Long> repos);
}
