package com.cusi.barberia_backend.repository;

import com.cusi.barberia_backend.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante,Long> {
    Optional<Estudiante> findByDni(String dni);
}
