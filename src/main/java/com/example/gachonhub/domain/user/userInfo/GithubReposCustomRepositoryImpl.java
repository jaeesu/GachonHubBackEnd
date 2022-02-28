package com.example.gachonhub.domain.user.userInfo;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.gachonhub.domain.user.userInfo.QGithubRepos.githubRepos;

@Repository
@RequiredArgsConstructor
public class GithubReposCustomRepositoryImpl implements GithubReposCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional
    public void updateMainRepository(List<Long> repos) {
        jpaQueryFactory.selectFrom(githubRepos)
                .where((githubRepos.id.in(repos)))
                .fetch()
                .forEach(x -> x.addMain());

    }

}
