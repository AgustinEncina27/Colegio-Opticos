package com.colegio.opticos.colegiocuotas.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatriculadoDTO {
	private Long id;
    private String nombre;
    private Integer matricula;
    private String dni;
    private String telefono;
    private LocalDate fechaRegistro;
    private String email;
    private boolean habilitado;
    private boolean pagoAprobado;
    private int cuotasImpagas;
}
