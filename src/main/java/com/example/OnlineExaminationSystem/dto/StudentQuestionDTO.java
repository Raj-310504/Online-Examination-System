package com.example.OnlineExaminationSystem.dto;

import com.example.OnlineExaminationSystem.enums.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentQuestionDTO {
    private Long questionId;
    private String questionText;
    private QuestionType type;
    private int marks;
    private List<StudentQuestionOptionDTO> options;
}
