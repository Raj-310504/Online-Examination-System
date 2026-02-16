package com.example.OnlineExaminationSystem.controller;

import com.example.OnlineExaminationSystem.dto.*;
import com.example.OnlineExaminationSystem.entity.StudentAnswer;
import com.example.OnlineExaminationSystem.service.StudentExamService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students/exams")
public class StudentExamController {

    private final StudentExamService studentExamService;

    public StudentExamController(StudentExamService studentExamService) {
        this.studentExamService = studentExamService;
    }

    @GetMapping("/active")
    public ResponseEntity<ActiveExamsResponse> getActiveExams() {
        List<ExamResponseDTO> exams = studentExamService.getActiveExams();
        String message = exams.isEmpty() ? "No active exams" : "Active exams found";
        return ResponseEntity.ok(new ActiveExamsResponse(message, exams));
    }

    @PostMapping("/start")
    public ResponseEntity<StartExamResponse> startExam(@Valid @RequestBody StartExamRequest request) {
        return ResponseEntity.ok(studentExamService.startExam(request));
    }

    @GetMapping("/{examId}/questions")
    public ResponseEntity<StudentExamQuestionsResponse> getExamQuestions(@PathVariable Long examId) {
        return ResponseEntity.ok(studentExamService.getExamQuestions(examId));
    }

    @PostMapping("/answers")
    public ResponseEntity<Map<String, Object>> submitAnswer(@Valid @RequestBody SubmitAnswerRequest request) {
        StudentAnswer savedAnswer = studentExamService.submitAnswer(request);
        return ResponseEntity.ok(Map.of(
                "message", "Answer saved successfully",
                "answerId", savedAnswer.getId()
        ));
    }

}
