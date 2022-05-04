package com.example.gachonhub.contest.infrastructure;

import com.example.gachonhub.contest.domain.PostContest;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.gachonhub.domain.contest.QPostContest.postContest;

@Repository
@RequiredArgsConstructor
public class PostContestCustomRepositoryImpl implements PostContestCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostContest> getLimitListById(Long id) {
        return jpaQueryFactory.selectFrom(postContest)
                .orderBy(postContest.id.desc())
                .limit(7L)
                .fetch();

    }
}
