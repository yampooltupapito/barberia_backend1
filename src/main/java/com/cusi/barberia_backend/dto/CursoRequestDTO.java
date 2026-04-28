package com.cusi.barberia_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CursoRequestDTO {
    public String nombre;
    public String descripcion;
    public String categoria;      // "GENERAL" o "ESPECIALIDAD"
    public String requerimiento;
    public Double costoCurso;
    public Boolean isActive;
}
