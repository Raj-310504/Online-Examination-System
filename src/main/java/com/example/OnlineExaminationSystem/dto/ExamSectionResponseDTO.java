package com.example.OnlineExaminationSystem.dto;

import com.example.OnlineExaminationSystem.enums.SectionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamSectionResponseDTO {
    private Long sectionId;
    private String sectionName;

    private SectionType type; // MCQ / DESCRIPTIVE
    private int duration;

    private boolean compulsory;
}
