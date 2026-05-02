package com.cusi.barberia_backend.controller;

import com.cusi.barberia_backend.dto.LoginRequestDTO;
import com.cusi.barberia_backend.dto.LoginResponseDTO;
import com.cusi.barberia_backend.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    /**
     * POST /api/auth/login
     * Body: { "nombreUsuario": "admin", "contrasena": "1234" }
     * Respuesta: { "token": "eyJ...", "nombreUsuario": "admin", "expiresInMs": 3600000 }
     *
     * Si las credenciales son incorrectas → 401 (manejado por GlobalExceptionHandler).
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO request) {
        // AuthenticationManager verifica usuario+contraseña contra la BD (con BCrypt)
        // Si falla lanza AuthenticationException → GlobalExceptionHandler devuelve 401
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getNombreUsuario(),
                        request.getContrasena()
                )
        );

        String token = jwtService.generateToken(authentication.getName());

        return ResponseEntity.ok(new LoginResponseDTO(
                token,
                authentication.getName(),
                expirationMs
        ));
    }
}