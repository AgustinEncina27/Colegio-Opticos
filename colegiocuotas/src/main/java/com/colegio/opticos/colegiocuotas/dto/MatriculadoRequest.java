package com.colegio.opticos.colegiocuotas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatriculadoRequest {
    private String nombre;
    private Integer matricula;
    private String dni;
    private String telefono;
    private String email;      
    private String password;    
    private boolean habilitado; 
}
