package com.colegio.opticos.colegiocuotas.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticiaDTO {
    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaPublicacion;
    private String imagenUrl;
    private String autorNombre;
}
