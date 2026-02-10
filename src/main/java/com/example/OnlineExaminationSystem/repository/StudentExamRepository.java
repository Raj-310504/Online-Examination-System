package com.example.OnlineExaminationSystem.repository;

import com.example.OnlineExaminationSystem.entity.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {
}
