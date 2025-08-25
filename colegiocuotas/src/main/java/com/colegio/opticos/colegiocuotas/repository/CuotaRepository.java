package com.colegio.opticos.colegiocuotas.repository;

import java.time.YearMonth;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.colegio.opticos.colegiocuotas.model.Cuota;
import com.colegio.opticos.colegiocuotas.model.Matriculado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CuotaRepository extends JpaRepository<Cuota, Long> {
	List<Cuota> findByMatriculado(Matriculado matriculado);
	
	boolean existsByMatriculadoAndPeriodo(Matriculado m, YearMonth periodo);
	
	Page<Cuota> findByMatriculado(Matriculado matriculado, Pageable pageable);
	
	@Query("SELECT c FROM Cuota c " +
		       "JOIN c.matriculado m " +
		       "WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
		       "OR CAST(m.matricula AS string) LIKE CONCAT('%', :filtro, '%')")
		Page<Cuota> buscarPorNombreOMatricula(@Param("filtro") String filtro, Pageable pageable);


}
