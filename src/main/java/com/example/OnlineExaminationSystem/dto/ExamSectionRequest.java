package com.example.OnlineExaminationSystem.dto;

import com.example.OnlineExaminationSystem.enums.SectionType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamSectionRequest {
    @NotBlank
    private String sectionName;

    @Positive
    private int duration;

    private boolean compulsory;

    @NotNull
    private SectionType type; // MCQ / DESCRIPTIVE

    @NotEmpty(message = "Section must contain at least one question")
    @Valid
    private List<QuestionRequest> questions;
}
