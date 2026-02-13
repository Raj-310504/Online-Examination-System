package com.example.OnlineExaminationSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StartExamResponse {
    private String message;
    private Long studentExamId;
    private Long examId;
    private String studentId;
}
