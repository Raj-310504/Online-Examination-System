package com.example.OnlineExaminationSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentLoginResponseDTO {
    private String message;
    private String studentId;
    private boolean firstLogin;
}
