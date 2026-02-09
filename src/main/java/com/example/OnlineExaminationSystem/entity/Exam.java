package com.example.OnlineExaminationSystem.entity;

import com.example.OnlineExaminationSystem.enums.ExamStatus;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String examName;

    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;

    @Positive
    private int duration;

    @Enumerated(EnumType.STRING)
    @NotNull
    private ExamStatus status;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL,
                orphanRemoval = true)
    @Valid
    private List<ExamSection> sections;
}
