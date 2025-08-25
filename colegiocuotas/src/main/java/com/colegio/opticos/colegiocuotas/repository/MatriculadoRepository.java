package com.colegio.opticos.colegiocuotas.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.colegio.opticos.colegiocuotas.model.Matriculado;
import com.colegio.opticos.colegiocuotas.model.Usuario;

public interface MatriculadoRepository extends JpaRepository<Matriculado, Long> {
	Optional<Matriculado> findByUsuario(Usuario usuario);
	
	@Query("SELECT MAX(m.matricula) FROM Matriculado m")
	Optional<Integer> findMaxMatricula();
	
	Optional<Matriculado> findByUsuarioEmail(String email);
	
	@Query("SELECT m FROM Matriculado m WHERE " +
		       "LOWER(m.nombre) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
		       "m.dni LIKE CONCAT('%', :query, '%') OR " +
		       "CAST(m.matricula AS string) LIKE CONCAT('%', :query, '%')")
		Page<Matriculado> buscarPorNombreDniOMatricula(@Param("query") String query, Pageable pageable);
	
	 // Buscar por nombre, matrícula o DNI (ignora mayúsculas/minúsculas)
    @Query("SELECT m FROM Matriculado m " +
           "WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :filtro, '%')) " +
           "OR CAST(m.matricula AS string) LIKE CONCAT('%', :filtro, '%') " +
           "OR m.dni LIKE CONCAT('%', :filtro, '%') " +
           "ORDER BY m.nombre ASC")
    List<Matriculado> buscar(String filtro);
	
	Optional<Matriculado> findByDni(String dni);

	boolean existsByDni(String dni);

	boolean existsByDniAndIdNot(String dni, Long id);

}
