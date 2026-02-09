package com.example.OnlineExaminationSystem.service;

import com.example.OnlineExaminationSystem.entity.Admin;
import com.example.OnlineExaminationSystem.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
                .orElseThrow(() -> new RuntimeException("Admin not found with id: " + id));
    }

    // login
    public Admin login(String username, String password) {
        Admin admin = adminRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username"));

        if(!passwordEncoder.matches(password,admin.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return admin;
    }
}
