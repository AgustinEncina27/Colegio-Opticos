package com.colegio.opticos.colegiocuotas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Table(name = "noticia_enlace")
public class NoticiaEnlace {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "noticia_id", nullable = false)
    private Noticia noticia;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String url;

    @Column(name = "orden", nullable = false)
    private Integer orden = 0;
}
