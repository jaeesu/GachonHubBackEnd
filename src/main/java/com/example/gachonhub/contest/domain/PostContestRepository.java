package com.example.gachonhub.contest.domain;

import com.example.gachonhub.contest.infrastructure.PostContestCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostContestRepository extends JpaRepository<PostContest, Long>, PostContestCustomRepository {

}
