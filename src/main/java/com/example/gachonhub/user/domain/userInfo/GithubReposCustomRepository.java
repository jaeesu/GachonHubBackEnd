package com.example.gachonhub.user.domain.userInfo;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GithubReposCustomRepository {

    void updateMainRepository(List<Long> repos);
}
