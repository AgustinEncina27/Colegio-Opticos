package com.colegio.opticos.colegiocuotas.service;


import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.colegio.opticos.colegiocuotas.dto.MatriculadoDTO;
import com.colegio.opticos.colegiocuotas.dto.MatriculadoRegistroResponse;
import com.colegio.opticos.colegiocuotas.dto.MatriculadoRequest;
import com.colegio.opticos.colegiocuotas.exception.DniDuplicadoException;
import com.colegio.opticos.colegiocuotas.mapper.MatriculadoMapper;
import com.colegio.opticos.colegiocuotas.model.Matriculado;
import com.colegio.opticos.colegiocuotas.model.Rol;
import com.colegio.opticos.colegiocuotas.model.Usuario;
import com.colegio.opticos.colegiocuotas.repository.MatriculadoRepository;
import com.colegio.opticos.colegiocuotas.repository.UsuarioRepository;

@Service
@RequiredArgsConstructor
public class MatriculadoService {

    private final UsuarioRepository usuarioRepository;
    private final MatriculadoRepository matriculadoRepository;
    private final PasswordEncoder passwordEncoder;
    private final MatriculadoMapper matriculadoMapper;
    
    public MatriculadoDTO getPerfil(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Matriculado matriculado = matriculadoRepository.findByUsuario(usuario)
            .orElseThrow(() -> new RuntimeException("Matriculado no encontrado"));

        return matriculadoMapper.toDTO(matriculado);
    }
    
    public Page<MatriculadoDTO> buscarPorNombreDniOMatricula(String query, Pageable pageable) {
    	return matriculadoRepository.buscarPorNombreDniOMatricula(query, pageable)
    	        .map(matriculadoMapper::toDTO);
    }
    
    public MatriculadoDTO obtenerPorId(Long id) {
        Matriculado m = matriculadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matriculado no encontrado"));

        return matriculadoMapper.toDTO(m);
    }
    
    public List<MatriculadoDTO> buscar(String filtro) {
        return matriculadoRepository.buscar(filtro)
                .stream()
                .map(matriculadoMapper::toDTO)
                .collect(Collectors.toList());
    }
    
    public Page<MatriculadoDTO> listarTodos(Pageable pageable) {
    	return matriculadoRepository.findAll(pageable)
    	        .map(matriculadoMapper::toDTO);
    }

    public MatriculadoRegistroResponse registrarMatriculado(MatriculadoRequest request) {
    	
    	if (matriculadoRepository.existsByDni(request.getDni())) {
            throw new DniDuplicadoException("Ya existe un matriculado con el DNI " + request.getDni());
        }
    	
        // ✅ Generar contraseña aleatoria
        String passwordPlano = generarPasswordAleatoria(10); // por ejemplo de 10 caracteres

        Integer ultimaMatricula = matriculadoRepository.findMaxMatricula().orElse(0);
        Integer nuevaMatricula = ultimaMatricula + 1;

        Usuario usuario = Usuario.builder()
                .username(nuevaMatricula.toString()) // 👈 NUEVO
                .email(request.getEmail())
                .password(passwordEncoder.encode(passwordPlano))
                .rol(Rol.MATRICULADO)
                .habilitado(request.isHabilitado())
                .build();

        usuarioRepository.save(usuario);

        Matriculado matriculado = Matriculado.builder()
                .nombre(request.getNombre())
                .matricula(nuevaMatricula)
                .dni(request.getDni())
                .telefono(request.getTelefono())
                .fechaRegistro(LocalDate.now())
                .pagoAprobado(request.isPagoAprobado())
                .usuario(usuario)
                .build();

        matriculadoRepository.save(matriculado);

        return matriculadoMapper.toRegistroResponse(matriculado, passwordPlano);
    }
    
    public MatriculadoRegistroResponse regenerarPassword(Long id) {
        Matriculado m = matriculadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matriculado no encontrado"));

        String nuevaPassword = generarPasswordAleatoria(10);

        Usuario u = m.getUsuario();
        u.setPassword(passwordEncoder.encode(nuevaPassword));
        usuarioRepository.save(u);

        return matriculadoMapper.toRegistroResponse(m, nuevaPassword);

    }
    


    public MatriculadoDTO editarMatriculado(Long id, MatriculadoRequest request) {
    	
    	if (matriculadoRepository.existsByDniAndIdNot(request.getDni(), id)) {
    	    throw new DniDuplicadoException("Ya existe otro matriculado con el DNI " + request.getDni());
    	}
    	
        Matriculado m = matriculadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el matriculado"));

        m.setNombre(request.getNombre());
        m.setDni(request.getDni());
        m.setTelefono(request.getTelefono());
        m.setPagoAprobado(request.isPagoAprobado());

        Usuario usuario = m.getUsuario();
        usuario.setEmail(request.getEmail());
        usuario.setHabilitado(request.isHabilitado());

        matriculadoRepository.save(m);
        usuarioRepository.save(usuario);

        return getPerfil(usuario.getUsername());
    }

    public void darDeBaja(Long id) {
        Matriculado m = matriculadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No existe el matriculado"));
        m.getUsuario().setHabilitado(false);
        usuarioRepository.save(m.getUsuario());
    }
    
    public void darDeAlta(Long id) {
        Matriculado matriculado = matriculadoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Matriculado no encontrado"));

        Usuario usuario = matriculado.getUsuario();
        if (usuario != null) {
            usuario.setHabilitado(true);
            usuarioRepository.save(usuario);
        }
    }
    
    private String generarPasswordAleatoria(int longitud) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < longitud; i++) {
            int index = (int) (Math.random() * caracteres.length());
            sb.append(caracteres.charAt(index));
        }
        return sb.toString();
    }
    
    public void eliminarDefinitivamente(Long id) {
        Matriculado matriculado = matriculadoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Matriculado no encontrado"));

        matriculadoRepository.delete(matriculado);
    }

    
}
