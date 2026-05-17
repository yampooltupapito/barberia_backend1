package com.cusi.barberia_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
// Maneja de forma global los errores de la aplicación, devolviendo respuestas JSON con información
// del error y el código HTTP adecuado.
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 401 — credenciales incorrectas en el login.
     */
    // Maneja errores de autenticación lanzados por Spring Security, devolviendo un mensaje genérico
    // para evitar revelar detalles sobre qué credenciales fueron incorrectas.
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
    // Maneja errores de lógica de negocio lanzados por los servicios,
    // analizando el mensaje de error para determinar el código HTTP más adecuado (404, 409 o 400) y
    // devolviendo una respuesta JSON con detalles del error.
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
    // Construye el cuerpo de la respuesta JSON con un formato consistente que incluye la marca de tiempo,
    // el código de estado, el mensaje de error y el mensaje específico del error,
    // y devuelve una ResponseEntity con el código HTTP adecuado.
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