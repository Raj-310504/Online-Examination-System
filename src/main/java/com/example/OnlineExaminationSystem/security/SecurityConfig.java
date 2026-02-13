package com.example.OnlineExaminationSystem.security;

import com.example.OnlineExaminationSystem.repository.AdminRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final AdminRepository adminRepository;

    public SecurityConfig(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public APIs
                        .requestMatchers(
                                "/api/admins/login",
                                "/api/students/login",
                                "/api/students/change-password",
                                "/api/admin/exams/test",
                                "/api/students/exams/**",
                                "/error"
                        ).permitAll()
                        .requestMatchers("/api/admin/exams/**").permitAll()

                        // allow first admin creation only if none exist
                        .requestMatchers(HttpMethod.POST, "/api/admins")
                        .access((authentication, context) -> new AuthorizationDecision(
                                adminRepository.count() == 0 || hasAdminRole(authentication.get())
                        ))

                        // Student APIs
//                        .requestMatchers("/api/students/change-password").hasRole("STUDENT")

                        // Admin APIs
                        .requestMatchers("/api/admins/**").hasRole("ADMIN")

                        // Everything else
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    private boolean hasAdminRole(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
