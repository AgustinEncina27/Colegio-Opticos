package com.colegio.opticos.colegiocuotas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpticaDTO {
    private Long id;
    private String nombre;
    private String propietario;
    private String direccion;
    private String telefono;
    private boolean habilitadaParaContactologia;
    
    private Long idLocalidad;
    private String nombreLocalidad;
    private String codigoPostal; 

    private Long idMatriculado;
    private String nombreOpticoTecnico;
    private Integer matriculaOpticoTecnico;
}
