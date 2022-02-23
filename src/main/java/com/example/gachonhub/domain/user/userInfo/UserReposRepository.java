package com.example.gachonhub.domain.user.userInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserReposRepository extends JpaRepository<UserRepos, Long>, UserReposCustomRepository {

}
