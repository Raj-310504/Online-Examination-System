package com.example.OnlineExaminationSystem.repository;

import com.example.OnlineExaminationSystem.entity.StudentAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StudentAnswerRepository extends JpaRepository<StudentAnswer, Long> {

    Optional<StudentAnswer> findByStudentExamIdAndQuestionId(Long studentExamId, Long questionId);

    @Query("select count(distinct sa.question.id) from StudentAnswer sa where sa.studentExam.id = :studentExamId")
    long countDistinctQuestionsByStudentExamId(@Param("studentExamId") Long studentExamId);
}
