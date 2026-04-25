package com.cusi.barberia_backend.repository;
import com.cusi.barberia_backend.entity.Horario;
import com.cusi.barberia_backend.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatriculaRepository extends JpaRepository<Matricula,Long> {
    Optional<Matricula> findTopByEstudianteDniOrderByCreatedAtDesc (String dni);
    List<Matricula> findByEstudianteDni(String dni);
    long countByHorarioIdAndEstado(Long horarioId,Matricula.MatriculaEstado estado);

}


