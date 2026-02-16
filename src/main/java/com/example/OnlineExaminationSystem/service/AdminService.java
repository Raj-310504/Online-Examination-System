package com.example.OnlineExaminationSystem.service;

import com.example.OnlineExaminationSystem.entity.Admin;
import com.example.OnlineExaminationSystem.exception.BadRequestException;
import com.example.OnlineExaminationSystem.exception.NotFoundException;
import com.example.OnlineExaminationSystem.repository.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin createAdmin(Admin admin) {

        admin.setRole("ROLE_ADMIN");

        // encode password
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        return adminRepository.save(admin);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Admin not found with id: " + id));
    }

    // login
    public Admin login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Invalid username"));

        if(!passwordEncoder.matches(password,admin.getPassword())) {
            throw new BadRequestException("Invalid password");
        }
        return admin;
    }
}
