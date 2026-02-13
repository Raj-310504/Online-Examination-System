package com.example.OnlineExaminationSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentExamQuestionsResponse {
    private Long examId;
    private String examName;
    private List<StudentSectionQuestionsDTO> sections;
}
