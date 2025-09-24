package com.anubis.backend.auth.dto;

import lombok.Data;

@Data
public class VerifyEmailDto {
    private String email;
    private String code;
}
