package com.example.OnlineExaminationSystem.dto;

import com.example.OnlineExaminationSystem.enums.ExamStatus;

import java.time.LocalDateTime;

public class ExamResponseDTO {

        private Long examId;
        private String examName;

        private LocalDateTime startTime;
        private LocalDateTime endTime;

        private int totalDuration;

        private ExamStatus status;

        private List<ExamSectionResponse> sections;
}
