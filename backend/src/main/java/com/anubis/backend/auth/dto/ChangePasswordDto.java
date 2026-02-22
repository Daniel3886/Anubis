package com.anubis.backend.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordDto {

    @NotNull(message = "Email is required")
    @Email(message = "Email is invalid")
    private String email;

    @NotNull(message = "Verification code is required")
    private String verificationCode;

    @NotNull(message = "New password is required")
    private String newPassword;

    @NotNull(message = "Repeated password is required")
    private String repeatedPassword;
}
