package com.cusi.barberia_backend.repository;


import com.cusi.barberia_backend.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repositorio para acceder y gestionar los cursos en la base de datos
public interface CursoRepository extends JpaRepository<Curso,Long> {
   //busca todoss los cursos qu estan activos
    List<Curso> findByIsActiveTrue();
}