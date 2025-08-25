package com.colegio.opticos.colegiocuotas.service;

import com.colegio.opticos.colegiocuotas.dto.NoticiaDTO;
import com.colegio.opticos.colegiocuotas.mapper.NoticiaMapper;
import com.colegio.opticos.colegiocuotas.model.Noticia;
import com.colegio.opticos.colegiocuotas.model.Usuario;
import com.colegio.opticos.colegiocuotas.repository.NoticiaRepository;
import com.colegio.opticos.colegiocuotas.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticiaService {

    private final NoticiaRepository noticiaRepository;
    private final UsuarioRepository usuarioRepository;
    private final NoticiaMapper noticiaMapper;
    private final ImagenService imagenService;

    private static final String BASE_URL = "/api/noticias/uploads/";

    public Page<NoticiaDTO> obtenerNoticias(Pageable pageable) {
        return noticiaRepository.findAll(pageable).map(noticia -> {
            NoticiaDTO dto = noticiaMapper.toDTO(noticia);
            if (dto.getImagenUrl() == null || dto.getImagenUrl().isBlank()) {
                dto.setImagenUrl(BASE_URL + "default.jpg");
            } else {
                dto.setImagenUrl(BASE_URL + dto.getImagenUrl());
            }
            return dto;
        });
    }

    public Optional<NoticiaDTO> obtenerNoticiaPorId(Long id) {
        return noticiaRepository.findById(id).map(noticia -> {
            NoticiaDTO dto = noticiaMapper.toDTO(noticia);
            if (dto.getImagenUrl() == null || dto.getImagenUrl().isBlank()) {
                dto.setImagenUrl(BASE_URL + "default.jpg");
            } else {
                dto.setImagenUrl(BASE_URL + dto.getImagenUrl());
            }
            return dto;
        });
    }

    public Noticia crearNoticia(String titulo, String descripcion, String nombreArchivo, Long autorId) {
        Usuario autor = usuarioRepository.findById(autorId)
                .orElseThrow(() -> new RuntimeException("Autor no encontrado"));

        Noticia noticia = Noticia.builder()
                .titulo(titulo)
                .descripcion(descripcion)
                .fechaPublicacion(LocalDateTime.now())
                .imagenUrl(nombreArchivo)
                .autor(autor)
                .build();

        return noticiaRepository.save(noticia);
    }

    public Noticia actualizarNoticia(Long id, String titulo, String descripcion, String nuevaImagenNombre) {
        Noticia noticia = noticiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Noticia no encontrada"));

        noticia.setTitulo(titulo);
        noticia.setDescripcion(descripcion);
        imagenService.eliminar(noticia.getImagenUrl());
        noticia.setImagenUrl(nuevaImagenNombre);

        return noticiaRepository.save(noticia);
    }

    public void eliminarNoticia(Long id) {
        Noticia noticia = noticiaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Noticia no encontrada"));

        imagenService.eliminar(noticia.getImagenUrl());
        noticiaRepository.delete(noticia);
    }
}