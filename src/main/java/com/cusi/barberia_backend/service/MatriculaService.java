package com.cusi.barberia_backend.service;

import com.cusi.barberia_backend.dto.MatriculaEstadoUpdateDTO;
import com.cusi.barberia_backend.dto.MatriculaRequestDTO;
import com.cusi.barberia_backend.dto.MatriculaResponseDTO;
import com.cusi.barberia_backend.entity.Estudiante;
import com.cusi.barberia_backend.entity.Horario;
import com.cusi.barberia_backend.entity.Matricula;
import com.cusi.barberia_backend.repository.EstudianteRepository;
import com.cusi.barberia_backend.repository.HorarioRepository;
import com.cusi.barberia_backend.repository.MatriculaRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
// Servicio para gestionar las matrículas, incluyendo creación, consulta y actualización de estado
@RequiredArgsConstructor
@Service
public class MatriculaService {
    // Repositorios para acceder a los datos de matrículas, estudiantes y horarios
    private final MatriculaRepository enrollmentRepository;
    private final EstudianteRepository studentRepository;
    private final HorarioRepository scheduleRepository;
    // Registra una nueva matrícula a partir de la solicitud del estudiante, validando datos y disponibilidad de cupos
    // Flujo público: estudiante envía su inscripción
    @Transactional
    public MatriculaResponseDTO createEnrollment(MatriculaRequestDTO request) {
         // Busca o crea al estudiante según su DNI, para evitar duplicados y reutilizar información existente
        // Si el estudiante ya existe por DNI lo reutiliza, si no lo crea
        Estudiante student = studentRepository.findByDni(request.getDni())
                .orElseGet(() -> {
                    Estudiante newStudent = new Estudiante();
                    newStudent.setDni(request.getDni());
                    newStudent.setNombre(request.getNombre());
                    newStudent.setApellido(request.getApellido());
                    newStudent.setTelefono(request.getTelefono());
                    newStudent.setCorreo(request.getCorreo());
                    return studentRepository.save(newStudent);
                });
          // Busca el horario seleccionado por el estudiante, para asociarlo a la matrícula y validar su existencia
        Horario schedule = scheduleRepository.findById(request.getHorarioId())
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        // Verifica que el estudiante no tenga una matrícula aprobada para el mismo curso, evitando inscripciones duplicadas en el mismo curso
        // Validar que haya cupos disponibles
        long occupied = enrollmentRepository
                .countByHorarioIdAndEstado(schedule.getId(), Matricula.MatriculaEstado.APROBADO);
        if (occupied >= schedule.getCupos()) {
            throw new RuntimeException("No hay cupos disponibles en este horario");
        }

        Matricula enrollment = new Matricula();
        enrollment.setEstudiante(student);
        enrollment.setHorario(schedule);
        enrollment.setComprobanteUrl(request.getReciboUrl());
        enrollment.setMontoPago(request.getMontoPago());
        enrollment.setEstado(Matricula.MatriculaEstado.PENDIENTE);

        enrollmentRepository.save(enrollment);
        return mapToResponseDTO(enrollment);
    }

    // Consulta la matrícula más reciente de un estudiante por su DNI, para mostrarle el estado de su inscripción
    // Flujo público: consulta por DNI
    public MatriculaResponseDTO getEnrollmentByDni(String dni) {
        Matricula enrollment = enrollmentRepository
                .findTopByEstudianteDniOrderByFechaMatriculaDesc(dni)
                .orElseThrow(() -> new RuntimeException("No se encontró una inscripción para ese DNI"));
        return mapToResponseDTO(enrollment);
    }
    // Lista todas las matriculas registradas
    // Admin: ver todas las matrículas
    public List<MatriculaResponseDTO> getAllEnrollments() {
        return enrollmentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    // Actualiza el estado de una matrícula (APROBADO o RECHAZADO) y añade un comentario opcional,
    // para que el admin pueda gestionar las inscripciones
    // Admin: aprobar o rechazar
    @Transactional
    public MatriculaResponseDTO updateEnrollmentStatus(Long id, MatriculaEstadoUpdateDTO request) {
        Matricula enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matrícula no encontrada"));

        enrollment.setEstado(Matricula.MatriculaEstado.valueOf(request.getEstado()));
        enrollment.setComentario(request.getComentario());

        enrollmentRepository.save(enrollment);
        return mapToResponseDTO(enrollment);
    }
    // Convierte una entidad Matricula a un DTO de respuesta,
    // incluyendo información del estudiante y del horario asociado para mostrar detalles
    // completos en las respuestas de la API
    private MatriculaResponseDTO mapToResponseDTO(Matricula enrollment) {
        MatriculaResponseDTO dto = new MatriculaResponseDTO();
        dto.setId(enrollment.getId());
        dto.setEstado(enrollment.getEstado().name());
        dto.setComentario(enrollment.getComentario());
        dto.setFechaMatricula(enrollment.getFechaMatricula());
        dto.setMontoPago(enrollment.getMontoPago());

        Estudiante s = enrollment.getEstudiante();
        dto.setNombreCompleto(s.getNombre() + " " + s.getApellido());
        dto.setEstudianteDni(s.getDni());

        Horario sc = enrollment.getHorario();
        dto.setNombreCurso(sc.getCurso().getNombre());
        dto.setHorarioDias(sc.getDias());
        dto.setFechaInicio(sc.getTiempoInicio());
        dto.setFechaFin(sc.getTiempoFinal());

        return dto;
    }
}
