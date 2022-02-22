package com.example.gachonhub.domain.contest;

import java.util.List;

public interface PostContestCustomRepository {

    List<PostContest> getLimitListById(Long id);
}
