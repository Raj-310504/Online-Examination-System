package com.example.OnlineExaminationSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuestionOptionRequest {

    @NotBlank
    private String optionText;

    private boolean correct;
}
