package com.anubis.backend.auth.service;
import com.anubis.backend.auth.dto.*;
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

    public String register(RegisterDto dto) {
        if (userRepo.existsUserByEmail(dto.getEmail())) {
            throw new RuntimeException("This email is already taken");
        }
        String code = generateVerificationCode();

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setVerified(false);
        user.setVerificationCode(code);

        userRepo.save(user);

        emailService.sendVerificationEmail(dto.getUsername(), dto.getEmail(), code);

        return "User registered successfully " + dto.getUsername() + ". Verification code sent to your email.";
    }

    public String verifyEmail(VerifyEmailDto dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (user.isVerified()) {
            return "Email already verified";
        }

        if (!user.getVerificationCode().equals(dto.getCode())) {
            throw new RuntimeException("Invalid verification code");
        }

        user.setVerified(true);
        user.setVerificationCode(null);
        userRepo.save(user);

        return "Email verified successfully!";
    }

    public String login(LoginDto dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        if(!user.isVerified()) {
            throw new RuntimeException("Please verify your email");
        }

        return "Login successful: " + user.getUsername();
    }

    public String forgotPassword(ForgotPasswordDto dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String code = generateVerificationCode();
        user.setVerificationCode(code);
        userRepo.save(user);

        emailService.sendForgotPasswordEmail(dto.getEmail(), code);

        return "A reset code has been sent to " + user.getEmail();
    }

    public String changePassword(ChangePasswordDto dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (user.getVerificationCode() == null || !user.getVerificationCode().equals(dto.getVerificationCode())) {
            throw new RuntimeException("Invalid or expired reset code");
        }

        if (!dto.getNewPassword().equals(dto.getRepeatedPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        if (passwordEncoder.matches(dto.getNewPassword(), user.getPassword())) {
            throw new RuntimeException("New password cannot be the same as the old one");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setVerificationCode(null);
        userRepo.save(user);

        return "Password reset successfully";
    }

    public String deleteAccount(DeleteAccountDto dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        userRepo.delete(user);

        return "Account deleted for: " + dto.getEmail();
    }


    private String generateVerificationCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }
}
