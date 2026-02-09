package com.example.OnlineExaminationSystem.service;

import com.example.OnlineExaminationSystem.entity.Student;
import com.example.OnlineExaminationSystem.enums.StudentStatus;
import com.example.OnlineExaminationSystem.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Student createStudent(Student student) {
        student.setRole("ROLE_STUDENT");
        student.setStatus(StudentStatus.ACTIVE);
        student.setFirstLogin(true);
        student.setPassword(passwordEncoder.encode(student.getPassword()));

        return studentRepository.save(student);
    }

    public Student login(String studentId, String password) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(()-> new RuntimeException("Invalid Student Id"));

        if (!passwordEncoder.matches(password, student.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }

        if(!student.getStatus().equals(StudentStatus.ACTIVE)) {
            throw new RuntimeException("Student is blocked");
        }
        return student;
    }

    // change password
    public void changePassword(String studentId, String oldPassword, String newPassword) {

        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        if (!passwordEncoder.matches(oldPassword, student.getPassword())) {
            throw new RuntimeException("Old password is incorrect");
        }

        student.setPassword(passwordEncoder.encode(newPassword));
        student.setFirstLogin(false);

        studentRepository.save(student);
    }
}
