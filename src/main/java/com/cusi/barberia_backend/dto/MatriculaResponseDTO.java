package com.cusi.barberia_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
@Getter
@Setter
public class MatriculaResponseDTO {
    public Long id;
    public String estado;
    public String comentario;
    public LocalDateTime fechaMatricula;
    public Double montoPago;
    public String nombreCompleto;
    public String estudianteDni;
    public String nombreCurso;
    public String horarioDias;
    public LocalTime fechaInicio;
    public LocalTime fechaFin;

}