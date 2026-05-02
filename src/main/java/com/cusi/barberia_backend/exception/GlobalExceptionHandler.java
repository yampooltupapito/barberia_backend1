package com.cusi.barberia_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 401 — credenciales incorrectas en el login.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthException(AuthenticationException ex) {
        return buildResponse(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas");
    }

    /**
     * Errores de negocio desde los services.
     *  "no encontrado/a"         → 404
     *  "No hay cupos / eliminar" → 409
     *  Cualquier otro            → 400
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Error inesperado";

        HttpStatus status;
        if (message.contains("no encontrado") || message.contains("no encontrada")) {
            status = HttpStatus.NOT_FOUND;
        } else if (message.contains("No hay cupos") || message.contains("No se puede eliminar")) {
            status = HttpStatus.CONFLICT;
        } else {
            status = HttpStatus.BAD_REQUEST;
        }

        return buildResponse(status, message);
    }

    private ResponseEntity<Map<String, Object>> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = Map.of(
                "timestamp", LocalDateTime.now().toString(),
                "status",    status.value(),
                "error",     status.getReasonPhrase(),
                "message",   message
        );
        return ResponseEntity.status(status).body(body);
    }
}