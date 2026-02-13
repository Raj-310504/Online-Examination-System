package com.example.OnlineExaminationSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private StudentExam studentExam;

    @ManyToOne
    @NotNull
    @JsonIgnore
    private Question question;

    private String answerText; // for short/long

    @ManyToOne
    @JoinColumn(name = "selected_option_id")
    @JsonIgnore
    private QuestionOption selectedOption; // for mcq
}
