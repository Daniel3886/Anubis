package com.anubis.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ForgotPasswordDto {
    @Email(message = "Email is invalid")
    @NotNull(message = "Email is required")
    private String email;
}
