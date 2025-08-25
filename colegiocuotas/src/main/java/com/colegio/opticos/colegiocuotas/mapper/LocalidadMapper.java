package com.colegio.opticos.colegiocuotas.mapper;

import com.colegio.opticos.colegiocuotas.dto.LocalidadDTO;
import com.colegio.opticos.colegiocuotas.model.Localidad;

public class LocalidadMapper {
    public static LocalidadDTO toDTO(Localidad localidad) {
        return new LocalidadDTO(
            localidad.getId(),
            localidad.getNombre(),
            localidad.getCodigoPostal()
        );
    }
}
