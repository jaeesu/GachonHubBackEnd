package com.example.gachonhub.domain.commitInfo;

import org.joda.time.DateTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CommitInfoRepository extends JpaRepository<CommitInfo, String> {

    Long countAllByUserId_Id(Long id);

    Long countAllByTeamId_Id(Long id);

    Long countAllByUserId_IdAndDateAfter(Long id, LocalDateTime date);

    Long countAllByTeamId_IdAndDateAfter(Long id, LocalDateTime date);
}
