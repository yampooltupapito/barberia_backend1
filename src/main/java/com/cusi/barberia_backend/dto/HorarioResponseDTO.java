package com.cusi.barberia_backend.dto;

import com.cusi.barberia_backend.entity.Curso;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
@Getter
@Setter
public class HorarioResponseDTO {

    private Long id;
    private String dias;
    private Curso curso;
    private LocalTime tiempoInicio;
    private LocalTime tiempoFinal;
    private Integer cupos;
    private Integer plazasDisponibles;
}
