package com.colegio.opticos.colegiocuotas.security;

import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.colegio.opticos.colegiocuotas.model.Usuario;
import com.colegio.opticos.colegiocuotas.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        if (!usuario.isHabilitado()) {
            throw new DisabledException("Usuario no habilitado");
        }
        
        // 👇 Asegurate de agregar el prefijo ROLE_
        List<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())
        );

        return new org.springframework.security.core.userdetails.User(
        	    usuario.getUsername(),   // ✅ Este es el valor que usaste en loginRequest.getUsername()
        	    usuario.getPassword(),
        	    authorities
        	);

    }
}