package com.cusi.barberia_backend.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="admins")
@NoArgsConstructor //crea un constructor sin parametros pero no visual
@Getter//para contruir getters pero no visual
@Setter//para contruir setter pero no visual
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name="nombre_usuario", nullable = false,unique = true)
    private String nombreUsuario;
    @Column(name = "password_hash",nullable = false)
    private String contraseña;



}
