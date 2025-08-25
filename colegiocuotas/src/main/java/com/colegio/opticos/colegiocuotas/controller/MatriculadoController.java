package com.colegio.opticos.colegiocuotas.controller;


import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.colegio.opticos.colegiocuotas.dto.MatriculadoDTO;
import com.colegio.opticos.colegiocuotas.dto.MatriculadoRegistroResponse;
import com.colegio.opticos.colegiocuotas.dto.MatriculadoRequest;
import com.colegio.opticos.colegiocuotas.service.MatriculadoService;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/matriculados")
@RequiredArgsConstructor
public class MatriculadoController {

    private final MatriculadoService matriculadoService;
    
    @GetMapping("/mi-perfil")
    public ResponseEntity<MatriculadoDTO> getMiPerfil(Authentication auth) {
        return ResponseEntity.ok(matriculadoService.getPerfil(auth.getName()));
    }
    
    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MatriculadoDTO>> listarTodos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
    	 Pageable pageable = PageRequest.of(page, size, Sort.by("matricula").ascending());
    	 return ResponseEntity.ok(matriculadoService.listarTodos(pageable));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatriculadoDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(matriculadoService.obtenerPorId(id));
    }
    
    @GetMapping("/buscar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MatriculadoDTO>> buscar(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(matriculadoService.buscarPorNombreDniOMatricula(query, pageable));
    }
    
    @GetMapping("/buscarMatriculados")
    public ResponseEntity<List<MatriculadoDTO>> buscarSinPaginacion(@RequestParam String filtro) {
        return ResponseEntity.ok(matriculadoService.buscar(filtro));
    }
    
    @PostMapping("/registrar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatriculadoRegistroResponse> registrar(@RequestBody MatriculadoRequest request) {
        var response = matriculadoService.registrarMatriculado(request);
        return ResponseEntity.ok(response);
    }
    
    @PutMapping("/{id}/regenerar-password")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatriculadoRegistroResponse> regenerarPassword(@PathVariable Long id) {
        return ResponseEntity.ok(matriculadoService.regenerarPassword(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatriculadoDTO> editar(@PathVariable Long id, @RequestBody MatriculadoRequest request) {
        return ResponseEntity.ok(matriculadoService.editarMatriculado(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> darDeBaja(@PathVariable Long id) {
        matriculadoService.darDeBaja(id);
        return ResponseEntity.noContent().build();
    }
    
    @PutMapping("/{id}/alta")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> darDeAlta(@PathVariable Long id) {
        matriculadoService.darDeAlta(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}/eliminar-definitivo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarDefinitivamente(@PathVariable Long id) {
        matriculadoService.eliminarDefinitivamente(id);
        return ResponseEntity.noContent().build();
    }
}
