package com.cusi.barberia_backend.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="estudiantes")
@NoArgsConstructor //crea un constructor sin parametros pero no visual
@Getter//para contruir getters pero no visual
@Setter//para contruir setter pero no visual
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String dni;
    private String nombre;
    private String apellido;
    private String telefono;
    private String correo;
    //matricula
    @OneToMany(mappedBy = "estudiante",cascade =CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Matricula>matriculas;


}
