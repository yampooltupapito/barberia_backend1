package com.cusi.barberia_backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class MatriculaRequestDTO {
    public String dni;
    public String nombre;
    public String apellido;
    public String telefono;
    public String correo;
    public Long horarioId;
    public String reciboUrl;
    public Double montoPago;

}
