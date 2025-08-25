package com.colegio.opticos.colegiocuotas.service;

import com.colegio.opticos.colegiocuotas.dto.LocalidadDTO;
import com.colegio.opticos.colegiocuotas.mapper.LocalidadMapper;
import com.colegio.opticos.colegiocuotas.repository.LocalidadRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocalidadService {

    private final LocalidadRepository localidadRepository;

    public List<LocalidadDTO> listarTodas() {
        return localidadRepository.findAll(Sort.by(Sort.Direction.ASC, "nombre"))
                .stream()
                .map(LocalidadMapper::toDTO)
                .toList();
    }

    public LocalidadDTO obtenerPorId(Long id) {
        return localidadRepository.findById(id)
                .map(LocalidadMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Localidad no encontrada"));
    }
}
