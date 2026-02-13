package com.example.OnlineExaminationSystem.controller;

import com.example.OnlineExaminationSystem.dto.ActiveExamsResponse;
import com.example.OnlineExaminationSystem.dto.ExamResponseDTO;
import com.example.OnlineExaminationSystem.dto.StartExamRequest;
import com.example.OnlineExaminationSystem.dto.StartExamResponse;
import com.example.OnlineExaminationSystem.dto.StudentExamQuestionsResponse;
import com.example.OnlineExaminationSystem.dto.SubmitAnswerRequest;
import com.example.OnlineExaminationSystem.entity.StudentAnswer;
import com.example.OnlineExaminationSystem.service.StudentExamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students/exams")
public class StudentExamController {

    @Autowired
    private StudentExamService  studentExamService;

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
