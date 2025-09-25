package com.anubis.backend.auth.dto;

import lombok.Data;

@Data
public class DeleteAccountDto {
    private String email;
    private String password;
}
