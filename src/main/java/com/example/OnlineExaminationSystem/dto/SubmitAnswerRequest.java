package com.example.OnlineExaminationSystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubmitAnswerRequest {

    @NotNull
    private Long studentExamId;

    @NotNull
    private Long questionId;

    private Long selectedOptionId;

    private String answerText;
}
