package com.cusi.barberia_backend.dto;

import com.cusi.barberia_backend.entity.Curso;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
@Getter
@Setter
public class HorarioResponseDTO {

    public Long id;
    public String dias;
    public Curso curso;
    public LocalTime tiempoInicio;
    public LocalTime tiempoFinal;
    public Integer cupos;
    public Integer plazasDisponibles;
}



