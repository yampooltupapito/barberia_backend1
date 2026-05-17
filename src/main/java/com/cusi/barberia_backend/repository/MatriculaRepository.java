package com.cusi.barberia_backend.repository;
import com.cusi.barberia_backend.entity.Horario;
import com.cusi.barberia_backend.entity.Matricula;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
// Repositorio para acceder y gestionar las matriculas en la base de datos
public interface MatriculaRepository extends JpaRepository<Matricula,Long> {
    // Busca la matrícula más reciente de un estudiante por su DNI,
    // ordenada por fecha de matrícula en orden descendente
    Optional<Matricula> findTopByEstudianteDniOrderByFechaMatriculaDesc (String dni);
    // Busca todas las matrículas de un estudiante por su DNI
    List<Matricula> findByEstudianteDni(String dni);
    // Cuenta las matrículas que tienen un horario específico y un estado específico
    long countByHorarioIdAndEstado(Long horarioId,Matricula.MatriculaEstado estado);

}


