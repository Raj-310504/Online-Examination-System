package com.example.OnlineExaminationSystem.entity;

import com.example.OnlineExaminationSystem.enums.ExamAttemptStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentExam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    @JsonIgnore
    private Student student;

    @ManyToOne
    @NotNull
    @JsonIgnore
    private Exam exam;

    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    @PositiveOrZero
    private int remainingTime;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ExamAttemptStatus status; // in_progress, submitted
}
