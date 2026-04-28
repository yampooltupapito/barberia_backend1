package com.cusi.barberia_backend.controller;

import com.cusi.barberia_backend.dto.MatriculaEstadoUpdateDTO;
import com.cusi.barberia_backend.dto.MatriculaRequestDTO;
import com.cusi.barberia_backend.dto.MatriculaResponseDTO;
import com.cusi.barberia_backend.service.MatriculaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matriculas")//ruta para ir a matricula
@RequiredArgsConstructor
public class MatriculaController {
    private final MatriculaService matriculaService;
    //para devolver un mensaje
    @PostMapping
    //metodo createMatricula
    public ResponseEntity<MatriculaResponseDTO>createMatricula(
            @RequestBody MatriculaRequestDTO request){
        MatriculaResponseDTO created = matriculaService.createEnrollment(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @GetMapping("/consulta")
    //metodo consultarPorDni
    public ResponseEntity<MatriculaResponseDTO>consultarPorDni(
            @RequestParam String dni){
        return ResponseEntity.ok(matriculaService.getEnrollmentByDni(dni));
    }
    //metodo getAllMatricula
    @GetMapping("/admin")
    public ResponseEntity<List<MatriculaResponseDTO>>getAllMatriculas(){
        return ResponseEntity.ok(matriculaService.getAllEnrollments());
    }
    //get updateEstado
    @PatchMapping("/admin/{id}/estado")
    public ResponseEntity<MatriculaResponseDTO>updateEstado(
            @PathVariable Long  id,
            @RequestBody MatriculaEstadoUpdateDTO request)   {
        return ResponseEntity.ok(matriculaService.updateEnrollmentStatus(id,request));
    }
    //eliminar matricula




}
