package com.cusi.barberia_backend.repository;


import com.cusi.barberia_backend.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
// Repositorio para acceder y gestionar los horarios en la base de datos
public interface HorarioRepository extends JpaRepository<Horario,Long> {
    // Busca los horarios que pertenezcan a un curso específico por su ID
    List<Horario> findByCursoId(Long id);
}
