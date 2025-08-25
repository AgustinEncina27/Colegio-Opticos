package com.colegio.opticos.colegiocuotas.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.colegio.opticos.colegiocuotas.model.Usuario;
import com.colegio.opticos.colegiocuotas.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {
	
    private final UsuarioRepository usuarioRepository;
	
	public Optional<Usuario> findByUsername(String username) {
	    return usuarioRepository.findByUsername(username);
	}
}
