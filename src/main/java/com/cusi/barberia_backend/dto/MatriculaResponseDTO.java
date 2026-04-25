package com.cusi.barberia_backend.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;
@Getter
@Setter
public class MatriculaResponseDTO {
    private Long id;
    private String estado;
    private String comentario;
    private LocalDateTime fechaMatricula;
    private Double montoPago;
    private String nombreCompleto;
    private String estudianteDni;
    private String nombreCurso;
    private String horarioDias;
    private LocalTime fechaInicio;
    private LocalTime fechaFin;



}
