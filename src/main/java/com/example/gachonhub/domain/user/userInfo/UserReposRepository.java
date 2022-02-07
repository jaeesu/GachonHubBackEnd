package com.example.gachonhub.domain.user.userInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserReposRepository extends JpaRepository<UserRepos, Long> {

    List<UserRepos> findAllByUser_IdAndMainIsTrue(Long id);

    @Query("select r from UserRepos r where r.id in :list")
    List<UserRepos> findAllByIds(@Param("list") List<Integer> ids);
}
