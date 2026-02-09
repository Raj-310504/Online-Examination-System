package com.example.OnlineExaminationSystem.repository;

import com.example.OnlineExaminationSystem.entity.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {
}
