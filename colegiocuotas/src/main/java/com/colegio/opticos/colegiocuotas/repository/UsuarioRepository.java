package com.colegio.opticos.colegiocuotas.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.opticos.colegiocuotas.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	Optional<Usuario> findByUsername(String username);
}
