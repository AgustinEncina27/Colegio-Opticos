package com.colegio.opticos.colegiocuotas.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatriculadoRegistroResponse {
    private String nombre;
    private Integer matricula;
    private String dni;
    private String telefono;
    private LocalDate fechaRegistro;
    private String email;
    private String username ;
    private String passwordPlano; // contraseña original
}
