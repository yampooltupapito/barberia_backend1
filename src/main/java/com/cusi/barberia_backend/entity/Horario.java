package com.cusi.barberia_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name="horarios")
@NoArgsConstructor //crea un constructor sin parametros pero no visual
@Getter//para contruir getters pero no visual
@Setter//para contruir setter pero no visual
public class Horario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="curso_id",nullable = false)
    private Curso curso;
    @Column(nullable = false)
    private String dias;
    @Column(name="tiempo_inicio")
    private LocalTime tiempoInicio;
    @Column(name="tiempo_final")
    private LocalTime tiempoFinal;
    private Integer cupos;
    //matriculas
    @OneToMany(mappedBy = "horario",cascade = CascadeType.ALL,fetch =FetchType.LAZY)
    private List<Matricula>matriculas;//llama ala clase



}
