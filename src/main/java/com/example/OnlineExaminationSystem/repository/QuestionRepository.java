package com.example.OnlineExaminationSystem.repository;

import com.example.OnlineExaminationSystem.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
