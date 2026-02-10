package com.example.OnlineExaminationSystem.service;

import com.example.OnlineExaminationSystem.dto.ExamResponseDTO;
import com.example.OnlineExaminationSystem.dto.ExamSectionResponseDTO;
import com.example.OnlineExaminationSystem.entity.Exam;
import com.example.OnlineExaminationSystem.enums.ExamStatus;
import com.example.OnlineExaminationSystem.repository.ExamRepository;
import com.example.OnlineExaminationSystem.repository.StudentExamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentExamService {

    @Autowired
    private StudentExamRepository studentExamRepository;

    @Autowired
    private ExamRepository examRepository;

    // get all active exams
    public List<ExamResponseDTO> getActiveExams() {
        LocalDateTime now = LocalDateTime.now();

        List<Exam> exams = examRepository
                .findByStatusAndStartTimeBeforeAndEndTimeAfter(
                        ExamStatus.ONGOING,
                        now,
                        now
                );

        return exams.stream()
                .map(this::mapToExamResponse)
                .toList();
    }

    // mapper
    private ExamResponseDTO mapToExamResponse(Exam exam) {

        ExamResponseDTO dto = new ExamResponseDTO();
        dto.setExamId(exam.getId());
        dto.setExamName(exam.getExamName());
        dto.setStartTime(exam.getStartTime());
        dto.setEndTime(exam.getEndTime());
        dto.setTotalDuration(exam.getDuration());
        dto.setStatus(exam.getStatus());

        List<ExamSectionResponseDTO> sectionDTOs =
                exam.getSections() == null ? List.of() :
                        exam.getSections().stream()
                                .map(section -> {
                                    ExamSectionResponseDTO sDto =
                                            new ExamSectionResponseDTO();
                                    sDto.setSectionId(section.getId());
                                    sDto.setSectionName(section.getSectionName());
                                    sDto.setType(section.getType());
                                    sDto.setDuration(section.getDuration());
                                    sDto.setCompulsory(section.isCompulsory());
                                    return sDto;
                                })
                                .toList();

        dto.setSections(sectionDTOs);
        return dto;
    }
}
