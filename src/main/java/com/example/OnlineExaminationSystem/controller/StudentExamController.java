package com.example.OnlineExaminationSystem.controller;

import com.example.OnlineExaminationSystem.dto.ExamResponseDTO;
import com.example.OnlineExaminationSystem.service.StudentExamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/students/exams")
public class StudentExamController {

    @Autowired
    private StudentExamService  studentExamService;

    @GetMapping("/active")
    public ResponseEntity<List<ExamResponseDTO>> getActiveExams() {
        return ResponseEntity.ok(studentExamService.getActiveExams());
    }
}
