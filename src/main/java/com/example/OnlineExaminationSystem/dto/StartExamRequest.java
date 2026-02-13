package com.example.OnlineExaminationSystem.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StartExamRequest {

    @NotBlank
    private String studentId;

    @NotNull
    private Long examId;
}
