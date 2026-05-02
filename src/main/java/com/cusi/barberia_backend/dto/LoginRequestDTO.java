package com.cusi.barberia_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDTO {
    private String nombreUsuario;
    private String contrasena;
}
