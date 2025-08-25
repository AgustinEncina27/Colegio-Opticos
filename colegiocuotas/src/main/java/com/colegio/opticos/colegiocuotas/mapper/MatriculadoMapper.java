package com.colegio.opticos.colegiocuotas.mapper;

import org.springframework.stereotype.Component;

import com.colegio.opticos.colegiocuotas.dto.MatriculadoDTO;
import com.colegio.opticos.colegiocuotas.dto.MatriculadoRegistroResponse;
import com.colegio.opticos.colegiocuotas.model.Matriculado;
import com.colegio.opticos.colegiocuotas.model.Usuario;

@Component
public class MatriculadoMapper {
    public  MatriculadoDTO toDTO(Matriculado m) {
        return MatriculadoDTO.builder()
            .id(m.getId())
            .nombre(m.getNombre())
            .matricula(m.getMatricula())
            .dni(m.getDni())
            .telefono(m.getTelefono())
            .fechaRegistro(m.getFechaRegistro())
            .email(m.getUsuario().getEmail())
            .habilitado(m.getUsuario().isHabilitado())
            .build();
    }
    
    public MatriculadoRegistroResponse toRegistroResponse(Matriculado m, String passwordPlano) {
        Usuario u = m.getUsuario();

        return MatriculadoRegistroResponse.builder()
                .nombre(m.getNombre())
                .matricula(m.getMatricula())
                .dni(m.getDni())
                .telefono(m.getTelefono())
                .fechaRegistro(m.getFechaRegistro())
                .email(u.getEmail())
                .username(u.getUsername())
                .passwordPlano(passwordPlano)
                .build();
    }
}