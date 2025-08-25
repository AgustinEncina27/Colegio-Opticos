package com.colegio.opticos.colegiocuotas.controller;

import com.colegio.opticos.colegiocuotas.dto.OpticaDTO;
import com.colegio.opticos.colegiocuotas.dto.OpticaRequest;
import com.colegio.opticos.colegiocuotas.service.OpticaService;
import lombok.RequiredArgsConstructor;


import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/opticas")
@RequiredArgsConstructor
public class OpticaController {

	private final OpticaService opticaService;

	@GetMapping("/listarPaginado")
	public ResponseEntity<Page<OpticaDTO>> listarPaginado(
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "15") int size) {
	    return ResponseEntity.ok(opticaService.listar(page, size));
	}
	
	@GetMapping("/buscar")
	public ResponseEntity<Page<OpticaDTO>> buscar(
	        @RequestParam String nombre,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(defaultValue = "15") int size) {
	    return ResponseEntity.ok(opticaService.buscarPorNombre(nombre, page, size));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<OpticaDTO> obtenerPorId(@PathVariable Long id) {
	    return ResponseEntity.ok(opticaService.obtenerPorId(id));
	}
	
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OpticaDTO> registrar(@RequestBody OpticaRequest request) {
        return ResponseEntity.ok(opticaService.registrar(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OpticaDTO> editar(@PathVariable Long id, @RequestBody OpticaRequest request) {
        return ResponseEntity.ok(opticaService.editar(id, request));
    }

    @PatchMapping("/{id}/contactologia")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestParam boolean habilitada) {
        opticaService.cambiarEstadoContactologia(id, habilitada);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        opticaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
