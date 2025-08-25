package com.colegio.opticos.colegiocuotas.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ImagenService {

    private final Logger log = LoggerFactory.getLogger(ImagenService.class);

    private static final String DIRECTORIO_UPLOAD = "uploads/noticias";

    public Resource cargar(String nombreFoto) throws MalformedURLException {
        if (nombreFoto == null || nombreFoto.isBlank()) {
            nombreFoto = "default.jpg";
        }

        // Extraer solo el nombre del archivo si viene con ruta (por seguridad y robustez)
        nombreFoto = Paths.get(nombreFoto).getFileName().toString();

        Path rutaArchivo = getPath(nombreFoto);
        log.info("Cargando imagen desde: {}", rutaArchivo.toString());

        Resource recurso = new UrlResource(rutaArchivo.toUri());

        if (!recurso.exists() || !recurso.isReadable()) {
            log.warn("No se pudo leer la imagen '{}'. Se usará imagen por defecto.", nombreFoto);

            rutaArchivo = Paths.get("src/main/resources/static/images").resolve("default.jpg").toAbsolutePath();
            recurso = new UrlResource(rutaArchivo.toUri());

            if (!recurso.exists() || !recurso.isReadable()) {
                throw new RuntimeException("No se pudo cargar la imagen por defecto.");
            }
        }

        return recurso;
    }

    public String copiar(MultipartFile archivo) throws IOException {
        String nombreArchivo = UUID.randomUUID().toString() + "_" + archivo.getOriginalFilename().replace(" ", "");
        Path rutaArchivo = getPath(nombreArchivo);
        log.info("Guardando imagen en: {}", rutaArchivo.toString());

        Files.createDirectories(rutaArchivo.getParent()); // Crea la carpeta si no existe
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        
        return nombreArchivo;
    }

    public boolean eliminar(String nombreFoto) {
        if (nombreFoto != null && !nombreFoto.isEmpty()) {
            Path rutaFoto = getPath(nombreFoto);
            File archivo = rutaFoto.toFile();
            if (archivo.exists() && archivo.canRead()) {
                archivo.delete();
                return true;
            }
        }
        return false;
    }

    public Path getPath(String nombreFoto) {
        return Paths.get(DIRECTORIO_UPLOAD).resolve(nombreFoto).toAbsolutePath();
    }
}
