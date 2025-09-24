package com.anubis.backend.auth.controller;

import com.anubis.backend.auth.dto.LoginDto;
import com.anubis.backend.auth.dto.RegisterDto;
import com.anubis.backend.auth.dto.VerifyEmailDto;
import com.anubis.backend.auth.service.AuthService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto) {
        String response = authService.register(
                registerDto.getUsername(),
                registerDto.getPassword(),
                registerDto.getEmail()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyEmail(@RequestBody VerifyEmailDto verifyEmailDto) {
        String response = authService.verifyEmail(
                verifyEmailDto.getEmail(),
                verifyEmailDto.getCode()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String response = authService.login(
                loginDto.getEmail(),
                loginDto.getPassword()
        );
        return ResponseEntity.ok(response);
    }
}
