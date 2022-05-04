package com.example.gachonhub.question.domain;

import com.example.gachonhub.question.domain.PostQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepository extends JpaRepository<PostQuestion, Long> {

}
