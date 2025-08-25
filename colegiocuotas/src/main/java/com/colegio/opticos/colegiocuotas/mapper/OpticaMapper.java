package com.colegio.opticos.colegiocuotas.mapper;

import org.springframework.stereotype.Component;

import com.colegio.opticos.colegiocuotas.dto.OpticaDTO;
import com.colegio.opticos.colegiocuotas.model.Localidad;
import com.colegio.opticos.colegiocuotas.model.Matriculado;
import com.colegio.opticos.colegiocuotas.model.Optica;

@Component
public class OpticaMapper {
	 public static OpticaDTO toDTO(Optica optica) {
	        if (optica == null) return null;

	        OpticaDTO dto = new OpticaDTO();
	        dto.setId(optica.getId());
	        dto.setNombre(optica.getNombre());
	        dto.setPropietario(optica.getPropietario());
	        dto.setDireccion(optica.getDireccion());
	        dto.setTelefono(optica.getTelefono());
	        dto.setHabilitadaParaContactologia(optica.isHabilitadaParaContactologia());
	        
	        if (optica.getLocalidad() != null) {
	            dto.setIdLocalidad(optica.getLocalidad().getId());
	            dto.setNombreLocalidad(optica.getLocalidad().getNombre());
	            dto.setCodigoPostal(optica.getLocalidad().getCodigoPostal()); 
	        }
	        
	        Matriculado m = optica.getOpticoTecnico();
	        if (m != null) {
	            dto.setIdMatriculado(m.getId());
	            dto.setNombreOpticoTecnico(m.getNombre());
	            dto.setMatriculaOpticoTecnico(m.getMatricula());
	        }

	        return dto;
	    }

	    public static Optica toEntity(OpticaDTO dto) {
	        if (dto == null) return null;

	        Optica optica = new Optica();
	        optica.setId(dto.getId());
	        optica.setNombre(dto.getNombre());
	        optica.setPropietario(dto.getPropietario());
	        optica.setDireccion(dto.getDireccion());
	        optica.setTelefono(dto.getTelefono());
	        optica.setHabilitadaParaContactologia(dto.isHabilitadaParaContactologia());
	    
	        if (dto.getIdLocalidad() != null) {
	            Localidad localidad = new Localidad();
	            localidad.setId(dto.getIdLocalidad());
	            optica.setLocalidad(localidad);
	        }
	        
	        // Nota: no se asigna Matriculado porque requiere fetch desde DB o servicio
	        return optica;
	    }
}
