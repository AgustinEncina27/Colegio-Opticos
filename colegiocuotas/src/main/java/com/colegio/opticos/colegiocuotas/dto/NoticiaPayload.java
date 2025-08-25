package com.colegio.opticos.colegiocuotas.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticiaPayload {
    private String titulo;
    private String descripcion;
    private String imagenUrl;
    private Long autorId; // en update puede ser opcional, o podés ignorarlo
}
