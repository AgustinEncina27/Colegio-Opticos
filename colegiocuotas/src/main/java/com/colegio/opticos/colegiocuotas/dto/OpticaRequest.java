package com.colegio.opticos.colegiocuotas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpticaRequest {
    private String nombre;
    private String propietario;
    private String direccion;
    private String telefono;
    private boolean habilitadaParaContactologia;
    private Long idLocalidad;
    private Long idMatriculado; // ID del óptico técnico
}
