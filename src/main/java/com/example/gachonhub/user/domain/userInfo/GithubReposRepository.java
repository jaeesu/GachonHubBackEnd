package com.example.gachonhub.user.domain.userInfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GithubReposRepository extends JpaRepository<GithubRepos, Long>, GithubReposCustomRepository {

}
