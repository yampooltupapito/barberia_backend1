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

@RequiredArgsConstructor
@Service
public class MatriculaService {
    private final MatriculaRepository enrollmentRepository;
    private final EstudianteRepository studentRepository;
    private final HorarioRepository scheduleRepository;

    // Flujo público: estudiante envía su inscripción
    @Transactional
    public MatriculaResponseDTO createEnrollment(MatriculaRequestDTO request) {

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

        Horario schedule = scheduleRepository.findById(request.getHorarioId())
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

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

    // Flujo público: consulta por DNI
    public MatriculaResponseDTO getEnrollmentByDni(String dni) {
        Matricula enrollment = enrollmentRepository
                .findTopByEstudianteDniOrderByCreatedAtDesc(dni)
                .orElseThrow(() -> new RuntimeException("No se encontró una inscripción para ese DNI"));
        return mapToResponseDTO(enrollment);
    }

    // Admin: ver todas las matrículas
    public List<MatriculaResponseDTO> getAllEnrollments() {
        return enrollmentRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

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
