package com.example.OnlineExaminationSystem.controller;

import com.example.OnlineExaminationSystem.dto.ExamCreateRequest;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/exams")
public class AdminExamController {

    @Autowired
    private ExamService examService;

    @PostMapping
    public ResponseEntity<Exam> createExam(
            @RequestBody @Valid ExamCreateRequest request
            ) {
        Exam exam = examService.createExam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(exam);
    }
}
