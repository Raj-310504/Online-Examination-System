package com.example.OnlineExaminationSystem.controller;

import com.example.OnlineExaminationSystem.dto.ExamCreateRequest;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.service.ExamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/exams")
public class AdminExamController {

    private final ExamService examService;

    public AdminExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping
    public ResponseEntity<Exam> createExam(
            @RequestBody @Valid ExamCreateRequest request
            ) {
        Exam exam = examService.createExam(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(exam);
    }

    @PostMapping("/status/refresh")
    public ResponseEntity<Map<String, Integer>> refreshExamStatuses() {
        int updated = examService.refreshExamStatuses();
        return ResponseEntity.ok(Map.of("updated", updated));
    }
}
