package com.cusi.barberia_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
@Getter
@Setter
public class HorarioRequestDTO {
    private String dias;
    private LocalTime tiempoInicio;
    private LocalTime tiempoFinal;
    private Integer cupos;
}

