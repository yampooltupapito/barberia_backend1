package com.cusi.barberia_backend.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CursoResponseDTO {
    public Long id;
    public String nombre;
    public String descripcion;
    public String categoria;
    public String requirimientos;
    public Double precioCurso;
    public List<HorarioResponseDTO> horarios;


}
