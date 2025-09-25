package com.anubis.backend.auth.dto;

import lombok.Data;

@Data
public class ChangePasswordDto {
    private String email;
    private String verificationCode;
    private String newPassword;
    private String repeatedPassword;
}
