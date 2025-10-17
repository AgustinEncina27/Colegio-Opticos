package com.colegio.opticos.colegiocuotas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "noticia_adjunto")
public class NoticiaAdjunto {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "noticia_id", nullable = false)
    private Noticia noticia;

    @Column(name = "file_name", length = 255, nullable = false)
    private String fileName;            // nombre interno guardado (UUID_nombre.ext)

    @Column(name = "original_name", length = 255, nullable = false)
    private String originalName;        // nombre original para mostrar

    @Column(name = "content_type", length = 120)
    private String contentType;

    @Column(name = "size_bytes", nullable = false)
    private Long sizeBytes;

    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;
}