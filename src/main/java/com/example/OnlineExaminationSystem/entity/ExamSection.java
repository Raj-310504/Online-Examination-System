package com.example.OnlineExaminationSystem.entity;

import com.example.OnlineExaminationSystem.enums.SectionType;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String sectionName;
    @Positive
    private int duration;
    private boolean compulsory;

    @Enumerated(EnumType.STRING)
    @NotNull
    private SectionType type; // mcq,descriptive

    @ManyToOne
    @NotNull
    private Exam exam;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL,
                orphanRemoval = true)
    @Valid
    private List<Question> questions;
}
