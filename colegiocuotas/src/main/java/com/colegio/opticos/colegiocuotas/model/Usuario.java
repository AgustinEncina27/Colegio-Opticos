package com.colegio.opticos.colegiocuotas.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(unique = true) // 👈 aseguramos que no se repita
    private String username;
	
	private String nombre;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol; // ADMIN o MATRICULADO

    private boolean habilitado; // 👈 Solo si es true puede ingresar

    @OneToOne(mappedBy = "usuario")
    private Matriculado matriculado;
}
