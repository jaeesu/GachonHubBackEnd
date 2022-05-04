package com.example.gachonhub.likes.domain;

import com.example.gachonhub.likes.domain.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Long> {


    void deleteByUser_IdAndPostQuestionId_Id(Long id, Long questionId);

    void deleteByUser_IdAndPostContest_Id(Long id, Long contestId);

    void deleteByUser_IdAndParentComment_Id(Long id, Long commentId);
}
