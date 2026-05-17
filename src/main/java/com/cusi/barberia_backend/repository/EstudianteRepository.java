package com.cusi.barberia_backend.repository;

import com.cusi.barberia_backend.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
// Repositorio para acceder y gestionar los estudiantes en la base de datos
public interface EstudianteRepository extends JpaRepository<Estudiante,Long> {
    // Busca un estudiante por su DNI
    Optional<Estudiante> findByDni(String dni);
}
