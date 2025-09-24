package com.anubis.backend.auth.service;
import com.anubis.backend.auth.entity.User;
import com.anubis.backend.auth.repo.UserRepo;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public AuthService(UserRepo userRepo, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public String register(String username, String password, String email) {
        if (userRepo.existsUserByEmail(email)) {
            throw new RuntimeException("This email is already taken");
        }

        String code = generateVerificationCode();

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setVerified(false);
        user.setVerificationCode(code);

        userRepo.save(user);

        emailService.sendVerificationEmail(username, email, code);

        return "User registered successfully " + username + ". Verification code sent to your email.";
    }


    public String verifyEmail(String email, String code) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (user.isVerified()) {
            return "Email already verified";
        }

        if (!user.getVerificationCode().equals(code)) {
            throw new RuntimeException("Invalid verification code");
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        userRepo.save(user);

        return "Email verified successfully!";
    }


    public String login(String email, String password) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if(!user.isVerified()) {
            throw new RuntimeException("Please verify your email");
        }

        return "Login successful: " + user.getUsername();
    }

    private String generateVerificationCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

}
