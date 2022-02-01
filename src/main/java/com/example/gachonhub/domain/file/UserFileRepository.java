package com.example.gachonhub.domain.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserFileRepository extends JpaRepository<UserFile, Long> {

    List<UserFile> findByPostQuestionId_Id(Long id);

    UserFile deleteByPostQuestionId_Id(Long id);

}
