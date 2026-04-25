package com.cusi.barberia_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="cursos")
@NoArgsConstructor //crea un constructor sin parametros pero no visual
@Getter//para contruir getters pero no visual
@Setter//para contruir setter pero no visual
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(columnDefinition = "TEXT")
    private String descripcion;
    @Column(name = "precio_curso")
    private Double precioCurso;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Categoria categoria;
    @Column(columnDefinition = "TEXT")
    private String requirimientos;
    @Column(name="is_active")
    private Boolean isActive;
    @OneToMany(mappedBy = "curso",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private List<Horario>horario;





    public enum Categoria{
        GENERAL ,ESPECIALIDAD
    }





}
