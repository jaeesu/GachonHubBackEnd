package com.example.gachonhub.domain.user.userInfo;

import com.example.gachonhub.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.gachonhub.domain.user.userInfo.QUserRepos.userRepos;

@Repository
@RequiredArgsConstructor
public class UserReposCustomRepositoryImpl implements UserReposCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    @Transactional
    public void updateMainRepository(User user, List<Long> repos) {
        jpaQueryFactory.selectFrom(userRepos)
                .where((userRepos.id.in(repos)).and(userRepos.user.id.eq(user.getId())))
                .fetch()
                .forEach(x -> x.addMain());

    }
}
