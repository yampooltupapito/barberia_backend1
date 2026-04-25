package com.cusi.barberia_backend.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CursoResponseDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String categoria;
    private String requirimientos;
    private Double precioCurso;
    private List<HorarioResponseDTO> horarios;


}
