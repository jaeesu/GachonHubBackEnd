package com.example.gachonhub.domain.user.userInfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubReposRepository extends JpaRepository<GithubRepos, Long>, GithubReposCustomRepository {

}
