package com.example.OnlineExaminationSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminLoginRequestDTO {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
