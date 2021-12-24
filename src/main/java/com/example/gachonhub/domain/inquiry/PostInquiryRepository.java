package com.example.gachonhub.domain.inquiry;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostInquiryRepository extends JpaRepository<PostInquiry, Long> {
}
