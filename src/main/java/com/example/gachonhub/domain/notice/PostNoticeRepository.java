package com.example.gachonhub.domain.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostNoticeRepository extends JpaRepository<PostNotice, Long> {
}
