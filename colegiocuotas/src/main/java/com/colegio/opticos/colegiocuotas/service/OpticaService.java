package com.colegio.opticos.colegiocuotas.service;

import com.colegio.opticos.colegiocuotas.dto.OpticaDTO;
import com.colegio.opticos.colegiocuotas.dto.OpticaRequest;
import com.colegio.opticos.colegiocuotas.mapper.OpticaMapper;
import com.colegio.opticos.colegiocuotas.model.Localidad;
import com.colegio.opticos.colegiocuotas.model.Matriculado;
import com.colegio.opticos.colegiocuotas.model.Optica;
import com.colegio.opticos.colegiocuotas.repository.LocalidadRepository;
import com.colegio.opticos.colegiocuotas.repository.MatriculadoRepository;
import com.colegio.opticos.colegiocuotas.repository.OpticaRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpticaService {

	private final OpticaRepository opticaRepository;
    private final MatriculadoRepository matriculadoRepository;
    private final LocalidadRepository localidadRepository; 

    public Page<OpticaDTO> listar(int page, int size) {
        Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by("localidad.nombre").ascending()
                .and(Sort.by("nombre").ascending())
        );
        
        return opticaRepository.findAll(pageable)
                .map(OpticaMapper::toDTO);
    }
    
    public Page<OpticaDTO> buscarPorNombre(String nombre, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("localidad.nombre").ascending().and(Sort.by("nombre").ascending()));
        return opticaRepository.findByNombreContainingIgnoreCase(nombre, pageable)
                .map(OpticaMapper::toDTO);
    }
    
    public OpticaDTO registrar(OpticaRequest request) {
        Matriculado m = matriculadoRepository.findById(request.getIdMatriculado())
                .orElseThrow(() -> new EntityNotFoundException("Matriculado no encontrado"));

        if (m.getOptica() != null) {
            throw new IllegalStateException("El matriculado ya está asociado a otra óptica");
        }

        Localidad localidad = localidadRepository.findById(request.getIdLocalidad())
                .orElseThrow(() -> new EntityNotFoundException("Localidad no encontrada")); 

        Optica optica = new Optica();
        optica.setNombre(request.getNombre());
        optica.setPropietario(request.getPropietario());
        optica.setDireccion(request.getDireccion());
        optica.setTelefono(request.getTelefono());
        optica.setHabilitadaParaContactologia(request.isHabilitadaParaContactologia());
        optica.setOpticoTecnico(m);
        optica.setLocalidad(localidad); // <-- Nuevo

        Optica guardada = opticaRepository.save(optica);
        m.setOptica(guardada);
        matriculadoRepository.save(m);

        return OpticaMapper.toDTO(guardada);
    }

    public OpticaDTO editar(Long id, OpticaRequest request) {
        Optica optica = opticaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Óptica no encontrada"));

        Matriculado nuevoOptico = matriculadoRepository.findById(request.getIdMatriculado())
                .orElseThrow(() -> new EntityNotFoundException("Matriculado no encontrado"));

        if (nuevoOptico.getOptica() != null && !nuevoOptico.getOptica().getId().equals(id)) {
            throw new IllegalStateException("El matriculado ya está asociado a otra óptica");
        }

        Localidad localidad = localidadRepository.findById(request.getIdLocalidad())
                .orElseThrow(() -> new EntityNotFoundException("Localidad no encontrada")); 

        optica.setNombre(request.getNombre());
        optica.setPropietario(request.getPropietario());
        optica.setDireccion(request.getDireccion());
        optica.setTelefono(request.getTelefono());
        optica.setHabilitadaParaContactologia(request.isHabilitadaParaContactologia());
        optica.setOpticoTecnico(nuevoOptico);
        optica.setLocalidad(localidad); 

        return OpticaMapper.toDTO(opticaRepository.save(optica));
    }

    public void cambiarEstadoContactologia(Long id, boolean habilitada) {
        Optica optica = opticaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Óptica no encontrada"));
        optica.setHabilitadaParaContactologia(habilitada);
        opticaRepository.save(optica);
    }

    public OpticaDTO obtenerPorId(Long id) {
        Optica optica = opticaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Óptica no encontrada"));

        return OpticaMapper.toDTO(optica);
    }
    
    public void eliminar(Long id) {
        Optica optica = opticaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Óptica no encontrada"));

        // Si el matriculado está asignado, lo desvinculamos antes de eliminar
        Matriculado opticoTecnico = optica.getOpticoTecnico();
        if (opticoTecnico != null) {
            opticoTecnico.setOptica(null);
            matriculadoRepository.save(opticoTecnico);
        }

        opticaRepository.delete(optica);
    }
}
