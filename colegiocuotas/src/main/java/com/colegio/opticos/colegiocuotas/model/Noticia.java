package com.colegio.opticos.colegiocuotas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String titulo;
    
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    private LocalDateTime fechaPublicacion;

    private String imagenUrl; // URL o nombre del archivo si está en tu servidor

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Usuario autor;
}
