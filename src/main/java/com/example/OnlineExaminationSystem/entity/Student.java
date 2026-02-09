package com.example.OnlineExaminationSystem.entity;

import com.example.OnlineExaminationSystem.enums.StudentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    @NotBlank
    private String studentId;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false,unique = true)
    @NotBlank
    @Email
    private String email;

    @Column(nullable = false)
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private StudentStatus status;

    @Column(nullable = false)
    @NotNull
    private Boolean firstLogin = true;

    private String role;
}
