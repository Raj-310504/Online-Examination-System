package com.example.OnlineExaminationSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLoginRequestDTO {
    @NotBlank
    private String studentId;
    @NotBlank
    private String password;
}
