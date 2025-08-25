package com.colegio.opticos.colegiocuotas.controller;

import com.colegio.opticos.colegiocuotas.dto.NoticiaDTO;
import com.colegio.opticos.colegiocuotas.dto.NoticiaPayload;
import com.colegio.opticos.colegiocuotas.model.Noticia;
import com.colegio.opticos.colegiocuotas.service.ImagenService;
import com.colegio.opticos.colegiocuotas.service.NoticiaService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.net.MalformedURLException;

@RestController
@RequestMapping("/api/noticias")
@RequiredArgsConstructor
public class NoticiaController {

    private final NoticiaService noticiaService;
    private final ImagenService imagenService;

    @GetMapping
    public Page<NoticiaDTO> listarNoticias(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size) {
    	return noticiaService.obtenerNoticias(PageRequest.of(page, size, Sort.by("fechaPublicacion").descending()));
	}

    @GetMapping("/{id}")
    public ResponseEntity<NoticiaDTO> verDetalle(@PathVariable Long id) {
        return noticiaService.obtenerNoticiaPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Noticia> crearNoticia(@RequestBody NoticiaPayload payload) {
        Noticia noticia = noticiaService.crearNoticia(
                payload.getTitulo(),
                payload.getDescripcion(),
                payload.getImagenUrl(),
                payload.getAutorId()
        );
        return ResponseEntity.ok(noticia);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Noticia> actualizarNoticia(@PathVariable Long id,
                                                     @RequestBody NoticiaPayload payload) {
        Noticia noticia = noticiaService.actualizarNoticia(
                id,
                payload.getTitulo(),
                payload.getDescripcion(),
                payload.getImagenUrl()
        );
        return ResponseEntity.ok(noticia);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarNoticia(@PathVariable Long id) {
        noticiaService.eliminarNoticia(id);
        return ResponseEntity.noContent().build();
    }

    
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> uploadImagen(@RequestParam("archivo") MultipartFile archivo) {
        Map<String, Object> response = new HashMap<>();

        if (!archivo.isEmpty()) {
            String nombreArchivo;
            try {
                nombreArchivo = imagenService.copiar(archivo);
            } catch (IOException e) {
                response.put("mensaje", "Error al subir la imagen");
                response.put("error", e.getMessage());
                return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            response.put("nombreArchivo", nombreArchivo);
            response.put("url", nombreArchivo); 
            response.put("mensaje", "Imagen subida correctamente");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }

        response.put("mensaje", "El archivo está vacío");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/uploads/{nombreFoto:.+}")
    public ResponseEntity<Resource> verFoto(@PathVariable String nombreFoto) {
        try {
            Resource recurso = imagenService.cargar(nombreFoto);

            HttpHeaders cabecera = new HttpHeaders();
            cabecera.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"");

            return new ResponseEntity<>(recurso, cabecera, HttpStatus.OK);
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
