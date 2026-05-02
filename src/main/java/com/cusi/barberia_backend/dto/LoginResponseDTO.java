package com.cusi.barberia_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String nombreUsuario;
    private long expiresInMs;
}