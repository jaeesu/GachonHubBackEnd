package com.example.gachonhub.commitInfo.domain;

import com.example.gachonhub.commitInfo.infrastructure.CommitInfoCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CommitInfoRepository extends JpaRepository<CommitInfo, String>, CommitInfoCustomRepository {


    Long countAllByUserId_Id(Long id);

    Long countAllByTeamId_Id(Long id);

    Long countAllByUserId_IdAndDateAfter(Long id, LocalDateTime date);

    Long countAllByTeamId_IdAndDateAfter(Long id, LocalDateTime date);
}
