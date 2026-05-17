package com.cusi.barberia_backend.service;

import com.cusi.barberia_backend.dto.CursoRequestDTO;
import com.cusi.barberia_backend.dto.CursoResponseDTO;
import com.cusi.barberia_backend.dto.HorarioRequestDTO;
import com.cusi.barberia_backend.dto.HorarioResponseDTO;
import com.cusi.barberia_backend.entity.Curso;
import com.cusi.barberia_backend.entity.Horario;
import com.cusi.barberia_backend.entity.Matricula;
import com.cusi.barberia_backend.repository.CursoRepository;
import com.cusi.barberia_backend.repository.HorarioRepository;
import com.cusi.barberia_backend.repository.MatriculaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service //esto ase q se comporte como un servicio
@RequiredArgsConstructor
public class CursoService {
    //la primera inyeccion de dependencias
    // Repositorios para acceder a los datos de cursos, horarios y matrículas
    private final CursoRepository cursoRepository;
    private final HorarioRepository horarioRepository;
    private final MatriculaRepository matriculaRepository;

    // Obtiene solo los cursos activos
    public List<CursoResponseDTO>getCursosActivos(){
        return cursoRepository.findByIsActiveTrue()
                .stream()
                .map(this::mapToCursoResponseDTO)
                .collect(Collectors.toList());
    }
    // Obtiene todos los cursos registrados
    public List<CursoResponseDTO>getCuros(){
        return cursoRepository.findAll()
                .stream()
                .map(this::mapToCursoResponseDTO)
                .collect(Collectors.toList());

    }
    // Crea un nuevo curso
    @Transactional
    public CursoResponseDTO createCourse(CursoRequestDTO request) {
        Curso course = new Curso();
        course.setNombre(request.getNombre());
        course.setDescripcion(request.getDescripcion());
        course.setCategoria(Curso.Categoria.valueOf(request.getCategoria()));
        course.setRequirimientos(request.getRequerimiento());
        course.setPrecioCurso(request.getCostoCurso());
        course.setIsActive(true);
        return mapToCursoResponseDTO(cursoRepository.save(course));
    }
    // Actualiza los datos de un curso existente (nombre, descripción, precio, estado activo)
    @Transactional
    public CursoResponseDTO updateCourse(Long id, CursoRequestDTO request) {
        Curso course = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        course.setNombre(request.getNombre());
        course.setDescripcion(request.getDescripcion());
        course.setCategoria(Curso.Categoria.valueOf(request.getCategoria()));
        course.setRequirimientos(request.getRequerimiento());
        course.setPrecioCurso(request.getCostoCurso());
        course.setIsActive(request.getIsActive());

        return mapToCursoResponseDTO(cursoRepository.save(course));
    }
    // Desactiva un curso sin eliminarlo de la base de datos (soft delete)
    // Soft delete: no borra de la BD, solo lo oculta
    @Transactional
    public void deactivateCourse(Long id) {
        Curso course = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        course.setIsActive(false);
        cursoRepository.save(course);
    }
     // Crea un horario para un curso específico
    // ── HORARIOS ─────────────────────────────────────────

    @Transactional
    public HorarioResponseDTO createSchedule(Long courseId, HorarioRequestDTO request) {
        Curso course = cursoRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Horario schedule = new Horario();
        schedule.setCurso(course);
        schedule.setDias(request.getDias());
        schedule.setTiempoInicio(request.getTiempoInicio());
        schedule.setTiempoFinal(request.getTiempoFinal());
        schedule.setCupos(request.getCupos());

        return mapToHorarioResponseDTO(horarioRepository.save(schedule));
    }
    // Actualiza un horario existente
    @Transactional
    public HorarioResponseDTO updateSchedule(Long scheduleId, HorarioRequestDTO request) {
        Horario schedule = horarioRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        schedule.setDias(request.getDias());
        schedule.setTiempoInicio(request.getTiempoInicio());
        schedule.setTiempoFinal(request.getTiempoFinal());
        schedule.setCupos(request.getCupos());

        return mapToHorarioResponseDTO(horarioRepository.save(schedule));
    }
    // Elimina un horario solo si no tiene matrículas registradas para evitar inconsistencias en la base de datos
    @Transactional
    public void deleteSchedule(Long scheduleId) {
        Horario schedule = horarioRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado"));

        // Previene borrar un horario que ya tiene matrículas
        boolean hasEnrollments = !schedule.getMatriculas().isEmpty();
        if (hasEnrollments) {
            throw new RuntimeException("No se puede eliminar un horario con matrículas registradas");
        }

        horarioRepository.delete(schedule);
    }

    // Convierte una entidad Curso a un DTO de respuesta, incluyendo sus horarios asociados
    public CursoResponseDTO mapToCursoResponseDTO(Curso curso){
        CursoResponseDTO dto =new CursoResponseDTO();
        dto.setId(curso.getId());
        dto.setNombre(curso.getNombre());
        dto.setDescripcion(curso.getDescripcion());
        dto.setCategoria(curso.getCategoria().name());
        dto.setRequirimientos(curso.getRequirimientos());
        dto.setPrecioCurso(curso.getPrecioCurso());
        List<HorarioResponseDTO>horarios=curso.getHorario()
                .stream()
                .map(s ->mapToHorarioResponseDTO(s))
                .collect(Collectors.toList());
        dto.setHorarios(horarios);
        return dto;

    }
    // Convierte una entidad Horario a un DTO de respuesta, calculando las plazas disponibles restando las matrículas aprobadas del total de cupos
    private HorarioResponseDTO mapToHorarioResponseDTO(Horario horario){
        HorarioResponseDTO dto =new HorarioResponseDTO();
        dto.setId(horario.getId());
        dto.setDias(horario.getDias());
        dto.setTiempoInicio(horario.getTiempoInicio());
        dto.setTiempoFinal(horario.getTiempoFinal());
        dto.setCupos(horario.getCupos());
        long ocupado=matriculaRepository
                .countByHorarioIdAndEstado(horario.getId(), Matricula.MatriculaEstado.APROBADO);
        dto.setPlazasDisponibles(horario.getCupos()-(int) ocupado);
        return dto;

    }


}
