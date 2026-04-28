package com.cusi.barberia_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
@Getter
@Setter
public class HorarioRequestDTO {
    public String dias;
    public LocalTime tiempoInicio;
    public LocalTime tiempoFinal;
    public Integer cupos;
}

