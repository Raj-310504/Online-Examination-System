package com.example.OnlineExaminationSystem.dto;

import com.example.OnlineExaminationSystem.enums.QuestionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionRequest {

    @NotBlank
    private String questionText;

    @NotNull
    private QuestionType type; // MCQ / SHORT / LONG

    @Positive
    private int marks;

    @Valid
    private List<QuestionOptionRequest> options; // only for MCQ
}
