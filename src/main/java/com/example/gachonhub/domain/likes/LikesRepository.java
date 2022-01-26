package com.example.gachonhub.domain.likes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {

    void deleteByUser_IdAndPostQuestion_Id(Long id, Long questionId);

    void deleteByUser_IdAndPostContest_Id(Long id, Long contestId);

    void deleteByUser_IdAndParentComment_Id(Long id, Long commentId);
}
