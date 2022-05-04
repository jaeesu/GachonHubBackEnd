package com.example.gachonhub.contest.infrastructure;

import com.example.gachonhub.contest.domain.PostContest;

import java.util.List;

public interface PostContestCustomRepository {

    List<PostContest> getLimitListById(Long id);
}
