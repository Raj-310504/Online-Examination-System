package com.example.OnlineExaminationSystem.controller;

import com.example.OnlineExaminationSystem.dto.*;
import com.example.OnlineExaminationSystem.entity.Student;
import com.example.OnlineExaminationSystem.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // admin use this
    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    // student login
    @PostMapping("/login")
    public ResponseEntity<StudentLoginResponseDTO> login(@Valid @RequestBody StudentLoginRequestDTO request) {
        Student student = studentService.login(
                request.getStudentId(),
                request.getPassword()
        );
        if (student.getFirstLogin()) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new StudentLoginResponseDTO(
                            "First login detected. Please change password.",
                            student.getStudentId(),
                            true
                    ));
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new StudentLoginResponseDTO(
                        "Login successful",
                        student.getStudentId(),
                        false
                ));
    }

    // first login password change
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@Valid @RequestBody ChangePasswordRequest request) {
        studentService.changePassword(
                request.getStudentId(),
                request.getOldPassword(),
                request.getNewPassword()
        );
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("Password changed successfully. Please login again.");
    }
}
