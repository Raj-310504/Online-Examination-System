package com.example.OnlineExaminationSystem.controller;

import com.example.OnlineExaminationSystem.dto.*;
import com.example.OnlineExaminationSystem.entity.Admin;
import com.example.OnlineExaminationSystem.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    // create admin
    @PostMapping
    public ResponseEntity<Admin> createAdmin(@Valid @RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.createAdmin(admin));
    }

    // get all admins
    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    // get by id
    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    // login
    @PostMapping("/login")
    public ResponseEntity<AdminLoginResponseDTO> login (@Valid @RequestBody AdminLoginRequestDTO request) {
        Admin admin = adminService.login(
                request.getUsername(),
                request.getPassword()
        );
        return ResponseEntity.ok(new AdminLoginResponseDTO(
                "Admin login successful",
                admin.getUsername()
        ));
    }
}
