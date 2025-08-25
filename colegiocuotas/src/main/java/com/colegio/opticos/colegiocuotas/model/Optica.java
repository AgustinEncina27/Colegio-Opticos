package com.colegio.opticos.colegiocuotas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Optica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String propietario;
    private String direccion;
    private String telefono;
    private boolean habilitadaParaContactologia;
    
    @ManyToOne
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    @OneToOne
    @JoinColumn(name = "matriculado_id", unique = true)
    private Matriculado opticoTecnico;
}