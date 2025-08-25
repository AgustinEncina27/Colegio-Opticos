package com.colegio.opticos.colegiocuotas.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.opticos.colegiocuotas.model.Noticia;

public interface NoticiaRepository extends JpaRepository<Noticia, Long> {
	Page<Noticia> findAllByOrderByFechaPublicacionDesc(Pageable pageable);
}
