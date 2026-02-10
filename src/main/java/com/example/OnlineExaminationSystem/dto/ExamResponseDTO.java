package com.example.OnlineExaminationSystem.dto;

import com.example.OnlineExaminationSystem.enums.ExamStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamResponseDTO {

        private Long examId;
        private String examName;

        private LocalDateTime startTime;
        private LocalDateTime endTime;

        private int totalDuration;

        private ExamStatus status;

        private List<ExamSectionResponseDTO> sections;
}
