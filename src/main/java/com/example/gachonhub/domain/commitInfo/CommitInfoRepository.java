package com.example.gachonhub.domain.commitInfo;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommitInfoRepository extends JpaRepository<CommitInfo, String> {

    Long countAllByUserId_Id(Long id);
}
