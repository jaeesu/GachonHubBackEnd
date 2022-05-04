package com.example.gachonhub.commitInfo.infrastructure;

import com.example.gachonhub.commitInfo.dto.CommitCountDto;

import java.util.List;

public interface CommitInfoCustomRepository {

    List<CommitCountDto> findCommitCountPerDayById(Long id);
}
