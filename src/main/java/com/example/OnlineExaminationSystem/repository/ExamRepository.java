package com.example.OnlineExaminationSystem.repository;

import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.enums.ExamStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    List<Exam> findByStatusAndStartTimeBeforeAndEndTimeAfter(ExamStatus examStatus, LocalDateTime start, LocalDateTime end);

    List<Exam> findByStartTimeBeforeAndEndTimeAfter(LocalDateTime start, LocalDateTime end);
}
