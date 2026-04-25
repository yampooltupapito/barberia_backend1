package com.cusi.barberia_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursoRequestDTO {
    private String nombre;
    private String descripcion;
    private String categoria;      // "GENERAL" o "ESPECIALIDAD"
    private String requerimiento;
    private Double costoCurso;
    private Boolean isActive;
}
