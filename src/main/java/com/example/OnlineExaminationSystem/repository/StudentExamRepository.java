package com.example.OnlineExaminationSystem.repository;

import com.example.OnlineExaminationSystem.entity.StudentExam;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentExamRepository extends JpaRepository<StudentExam, Long> {
    Optional<StudentExam> findByStudentIdAndExamId(Long studentId, Long examId);
}
