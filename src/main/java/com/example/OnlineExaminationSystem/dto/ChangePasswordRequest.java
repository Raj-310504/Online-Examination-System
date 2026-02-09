package com.example.OnlineExaminationSystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank
    private String studentId;
    @NotBlank
    @Size(min = 6, max = 100)
    private String oldPassword;
    @NotBlank
    @Size(min = 6, max = 100)
    private String newPassword;
}
