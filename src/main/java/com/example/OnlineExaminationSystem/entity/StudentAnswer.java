package com.example.OnlineExaminationSystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull
    private StudentExam studentExam;

    @ManyToOne
    @NotNull
    private Question question;

    private String answerText; // for short/long

    @ManyToOne
    private QuestionOption selectedOptionId; // for mcq
}
