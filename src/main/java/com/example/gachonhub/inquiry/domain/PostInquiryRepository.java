package com.example.gachonhub.inquiry.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostInquiryRepository extends JpaRepository<PostInquiry, Long> {
}
