package com.example.OnlineExaminationSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentSectionQuestionsDTO {
    private Long sectionId;
    private String sectionName;
    private List<StudentQuestionDTO> questions;
}
