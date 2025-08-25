package com.colegio.opticos.colegiocuotas.mapper;

import com.colegio.opticos.colegiocuotas.dto.NoticiaDTO;
import com.colegio.opticos.colegiocuotas.model.Noticia;
import org.springframework.stereotype.Component;

@Component
public class NoticiaMapper {

    public NoticiaDTO toDTO(Noticia noticia) {
        return NoticiaDTO.builder()
                .id(noticia.getId())
                .titulo(noticia.getTitulo())
                .descripcion(noticia.getDescripcion())
                .fechaPublicacion(noticia.getFechaPublicacion())
                .imagenUrl(noticia.getImagenUrl())
                .autorNombre(noticia.getAutor().getNombre())
                .build();
    }
}
