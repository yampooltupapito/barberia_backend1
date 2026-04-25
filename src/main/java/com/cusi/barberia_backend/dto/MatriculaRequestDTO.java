package com.cusi.barberia_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MatriculaRequestDTO {
    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    private Long horarioId;
    private String reciboUrl;
    private Double montoPago;

}
