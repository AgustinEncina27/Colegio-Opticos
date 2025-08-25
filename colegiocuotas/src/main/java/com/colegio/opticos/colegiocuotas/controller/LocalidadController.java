package com.colegio.opticos.colegiocuotas.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.opticos.colegiocuotas.dto.LocalidadDTO;
import com.colegio.opticos.colegiocuotas.service.LocalidadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/localidades")
@RequiredArgsConstructor
public class LocalidadController {

	private final LocalidadService localidadService;

    @GetMapping
    public List<LocalidadDTO> listarTodas() {
        return localidadService.listarTodas();
    }

    @GetMapping("/{id}")
    public LocalidadDTO obtenerPorId(@PathVariable Long id) {
        return localidadService.obtenerPorId(id);
    }
}
