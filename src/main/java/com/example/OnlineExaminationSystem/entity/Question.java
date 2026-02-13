package com.example.OnlineExaminationSystem.entity;

import com.example.OnlineExaminationSystem.dto.QuestionRequest;
import com.example.OnlineExaminationSystem.enums.QuestionType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String questionText;

    @Enumerated(EnumType.STRING)
    @NotNull
    private QuestionType type;// mcq,short,long

    @Positive
    private int marks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    @NotNull
    @JsonIgnore
    private ExamSection section;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<QuestionOption> options;
}
