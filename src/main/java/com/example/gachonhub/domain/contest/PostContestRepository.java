package com.example.gachonhub.domain.contest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostContestRepository extends JpaRepository<PostContest, Long> {

}
