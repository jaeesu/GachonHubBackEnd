package com.example.gachonhub.commitInfo.infrastructure;

import com.example.gachonhub.commitInfo.dto.CommitCountDto;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static com.example.gachonhub.domain.commitInfo.QCommitInfo.commitInfo;

@Repository
@RequiredArgsConstructor
public class CommitInfoCustomRepositoryImpl implements CommitInfoCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommitCountDto> findCommitCountPerDayById(Long id) {
        StringTemplate dateTemplate = Expressions.stringTemplate(
                "DATE_FORMAT({0}, {1})",
                commitInfo.date,
                ConstantImpl.create("%Y-%m-%d")
        );

        List<CommitCountDto> commitCountDtoList = jpaQueryFactory.select(
                        Projections.constructor(CommitCountDto.class,
                                dateTemplate.as("date"),
                                commitInfo.count().as("count")
                        )
                )
                .from(commitInfo)
                .where(commitInfo.userId.id.eq(id).and(
                        commitInfo.date.goe(LocalDate.of(2022,1,1).atStartOfDay())
                ))
                .groupBy(dateTemplate)
                .fetch();
        return commitCountDtoList;
    }
}
