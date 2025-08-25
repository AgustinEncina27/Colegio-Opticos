package com.colegio.opticos.colegiocuotas.repository;

import com.colegio.opticos.colegiocuotas.model.Optica;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpticaRepository extends JpaRepository<Optica, Long> {
	Page<Optica> findByNombreContainingIgnoreCase(String nombre, Pageable pageable);

}
