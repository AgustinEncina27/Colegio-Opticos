package com.colegio.opticos.colegiocuotas.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matriculado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer matricula;
    private String dni;
    private String telefono;
    private LocalDate fechaRegistro;
    
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean pagoAprobado;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @OneToMany(mappedBy = "matriculado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cuota> cuotas;
    
    @OneToOne(mappedBy = "opticoTecnico")
    private Optica optica;
}
