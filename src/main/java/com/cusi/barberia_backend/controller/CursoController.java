package com.cusi.barberia_backend.controller;

import com.cusi.barberia_backend.dto.CursoRequestDTO;
import com.cusi.barberia_backend.dto.CursoResponseDTO;
import com.cusi.barberia_backend.dto.HorarioRequestDTO;
import com.cusi.barberia_backend.dto.HorarioResponseDTO;
import com.cusi.barberia_backend.service.CursoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cursos")
@RequiredArgsConstructor
public class CursoController {

    private final CursoService cursoService;


    /**
     * Público: devuelve solo los cursos activos para mostrar en la web.
     */
    @GetMapping
    public ResponseEntity<List<CursoResponseDTO>> getCursosActivos() {
        return ResponseEntity.ok(cursoService.getCursosActivos());
    }

    /**
     * Admin: devuelve todos los cursos, incluyendo los inactivos.
     */
    @GetMapping("/admin/todos")
    public ResponseEntity<List<CursoResponseDTO>> getTodosCursos() {
        return ResponseEntity.ok(cursoService.getCuros());
    }

    /**
     * Admin: crea un nuevo curso.
     */
    @PostMapping("/admin")
    public ResponseEntity<CursoResponseDTO> createCurso(@RequestBody CursoRequestDTO request) {
        CursoResponseDTO created = cursoService.createCourse(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Admin: actualiza un curso existente (nombre, descripción, precio, estado activo).
     */
    @PutMapping("/admin/{id}")
    public ResponseEntity<CursoResponseDTO> updateCurso(
            @PathVariable Long id,
            @RequestBody CursoRequestDTO request) {
        return ResponseEntity.ok(cursoService.updateCourse(id, request));
    }

    /**
     * Admin: soft-delete, marca el curso como inactivo sin borrarlo de la BD.
     */
    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Void> deactivateCurso(@PathVariable Long id) {
        cursoService.deactivateCourse(id);
        return ResponseEntity.noContent().build();
    }

    // ── HORARIOS ─────────────────────────────────────────

    /**
     * Admin: añade un horario a un curso específico.
     */
    @PostMapping("/admin/{cursoId}/horarios")
    public ResponseEntity<HorarioResponseDTO> createHorario(
            @PathVariable Long cursoId,
            @RequestBody HorarioRequestDTO request) {
        HorarioResponseDTO created = cursoService.createSchedule(cursoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Admin: actualiza días, horario y cupos de un horario.
     */
    @PutMapping("/admin/horarios/{horarioId}")
    public ResponseEntity<HorarioResponseDTO> updateHorario(
            @PathVariable Long horarioId,
            @RequestBody HorarioRequestDTO request) {
        return ResponseEntity.ok(cursoService.updateSchedule(horarioId, request));
    }

    /**
     * Admin: elimina un horario solo si no tiene matrículas asociadas.
     * El service lanza RuntimeException si tiene matrículas; el handler global la convierte en 409.
     */
    @DeleteMapping("/admin/horarios/{horarioId}")
    public ResponseEntity<Void> deleteHorario(@PathVariable Long horarioId) {
        cursoService.deleteSchedule(horarioId);
        return ResponseEntity.noContent().build();
    }
}
