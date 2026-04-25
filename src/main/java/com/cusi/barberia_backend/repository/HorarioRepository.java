package com.cusi.barberia_backend.repository;


import com.cusi.barberia_backend.entity.Horario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HorarioRepository extends JpaRepository<Horario,Long> {
    //ordenar horarios por cursos
    List<Horario> findByCursoId(Long id);
}
