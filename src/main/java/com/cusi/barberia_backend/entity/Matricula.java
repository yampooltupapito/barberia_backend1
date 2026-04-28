package com.cusi.barberia_backend.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name="matriculas")
@NoArgsConstructor //crea un constructor sin parametros pero no visual
@Getter//para contruir getters pero no visual
@Setter//para contruir setter pero no visual
public class Matricula {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id" ,nullable = false)
    private Estudiante estudiante;
    @ManyToOne(fetch =FetchType.LAZY )
    @JoinColumn(name = "horario_id" ,nullable = false)
    private Horario horario;
    @Column(name="comprobante_url")
    private String comprobanteUrl;
    @Column(name = "monto_pago")
    private Double montoPago;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MatriculaEstado estado=MatriculaEstado.PENDIENTE;
    @Column(name="fecha_matricula",nullable = false,updatable = false)
    private LocalDateTime fechaMatricula;
    private String comentario;

    @PrePersist
    public void prePersist(){
        this.fechaMatricula=LocalDateTime.now();

    }
    public enum MatriculaEstado{
            PENDIENTE,APROBADO,RECHAZADO
    }



}
