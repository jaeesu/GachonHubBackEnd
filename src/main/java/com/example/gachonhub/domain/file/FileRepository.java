package com.example.gachonhub.domain.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<UserFile, Long> {

    List<UserFile> findByPostQuestionId_Id(Long id);

}
