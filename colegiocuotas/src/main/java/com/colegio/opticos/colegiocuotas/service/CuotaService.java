package com.colegio.opticos.colegiocuotas.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import com.colegio.opticos.colegiocuotas.dto.CuotaDTO;
import com.colegio.opticos.colegiocuotas.dto.MatriculadoDTO;
import com.colegio.opticos.colegiocuotas.mapper.MatriculadoMapper;
import com.colegio.opticos.colegiocuotas.model.Cuota;
import com.colegio.opticos.colegiocuotas.model.EstadoCuota;
import com.colegio.opticos.colegiocuotas.model.Matriculado;
import com.colegio.opticos.colegiocuotas.model.Usuario;
import com.colegio.opticos.colegiocuotas.repository.CuotaRepository;
import com.colegio.opticos.colegiocuotas.repository.MatriculadoRepository;
import com.colegio.opticos.colegiocuotas.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class CuotaService {

    private final UsuarioRepository usuarioRepository;
    private final MatriculadoRepository matriculadoRepository;
    private final CuotaRepository cuotaRepository;
    private final MatriculadoMapper matriculadoMapper;
    
    @Value("${cuota.monto.mensual}")
    private BigDecimal montoMensual;
    
    
    public Page<CuotaDTO> obtenerCuotasPaginadas(String username,int page, int size) {
    	Usuario usuario = usuarioRepository.findByUsername(username).orElseThrow();
    	Matriculado matriculado = matriculadoRepository.findByUsuario(usuario).orElseThrow();

        Pageable pageable = PageRequest.of(page, size, Sort.by("fechaVencimiento").descending());
        Page<Cuota> cuotas = cuotaRepository.findByMatriculado(matriculado, pageable);

        return cuotas.map(this::convertirADTO);
    }
    
    public void marcarComoPagada(Long idCuota) {
        Cuota cuota = cuotaRepository.findById(idCuota)
                .orElseThrow(() -> new RuntimeException("Cuota no encontrada"));

        cuota.setFechaPago(LocalDate.now());
        cuota.setEstado(EstadoCuota.PAGADA);
        cuotaRepository.save(cuota);
    }
    
    public void revertirPago(Long idCuota) {
        Cuota cuota = cuotaRepository.findById(idCuota)
                .orElseThrow(() -> new RuntimeException("Cuota no encontrada"));

        cuota.setFechaPago(null);
        cuota.setEstado(EstadoCuota.PENDIENTE);
        cuotaRepository.save(cuota);
    }
    
    public Page<MatriculadoDTO> obtenerMatriculadosMorosos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        List<Matriculado> todos = matriculadoRepository.findAll();

        List<MatriculadoDTO> morososDTO = todos.stream()
            .filter(m -> m.getUsuario() != null && m.getUsuario().isHabilitado()) // Solo habilitados
            .filter(m -> m.getCuotas().stream()
                .anyMatch(c -> (c.getEstado() == EstadoCuota.VENCIDA || c.getEstado() == EstadoCuota.PENDIENTE)
                        && c.getFechaPago() == null))
            .map(m -> {
                MatriculadoDTO dto = matriculadoMapper.toDTO(m);

                long cuotasImpagas = m.getCuotas().stream()
                    .filter(c -> (c.getEstado() == EstadoCuota.VENCIDA || c.getEstado() == EstadoCuota.PENDIENTE)
                            && c.getFechaPago() == null)
                    .count();

                dto.setCuotasImpagas((int) cuotasImpagas); // Nuevo campo en el DTO
                return dto;
            })
            .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), morososDTO.size());
        List<MatriculadoDTO> pagedList = morososDTO.subList(start, end);

        return new PageImpl<>(pagedList, pageable, morososDTO.size());
    }
    
    public Page<CuotaDTO> listarCuotasParaAdmin(String filtro, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.desc("periodo"), Sort.Order.desc("id")));
    	return cuotaRepository.buscarPorNombreOMatricula(filtro, pageable)
    	                     .map(this::convertirADTO);
    }
    
    @Scheduled(cron = "0 5 0 1 * *")
    public void generarCuotasMensuales() {
        YearMonth mesAProcesar = YearMonth.now();
        YearMonth mesAnterior   = mesAProcesar.minusMonths(1);
        LocalDate vencimiento   = mesAProcesar.atDay(10);

        log.info("🔄 Generando cuotas para el mes {}", mesAProcesar);

        List<Matriculado> matriculados = matriculadoRepository.findAll();

        for (Matriculado m : matriculados) {

            // Saltar matriculados cuyo usuario esté deshabilitado
            if (m.getUsuario() == null || !m.getUsuario().isHabilitado()) {
                continue;
            }

            // Saltar si fue registrado este mes o el anterior
            if (m.getFechaRegistro() != null) {
                YearMonth registro = YearMonth.from(m.getFechaRegistro());
                if (registro.equals(mesAProcesar) || registro.equals(mesAnterior)) {
                    continue;
                }
            }

            // Evitar duplicados
            if (cuotaRepository.existsByMatriculadoAndPeriodo(m, mesAProcesar)) {
                continue;
            }

            // 👇 Lógica nueva: si el matriculado tiene pagoAprobado => crear cuota PAGADA
            boolean pagoAprobado = m.isPagoAprobado();
            EstadoCuota estado   = pagoAprobado ? EstadoCuota.PAGADA : EstadoCuota.PENDIENTE;
            LocalDate fechaPago  = pagoAprobado ? LocalDate.now() : null;

            Cuota cuota = Cuota.builder()
                    .matriculado(m)
                    .monto(montoMensual)             // BigDecimal definido en tu clase
                    .fechaVencimiento(vencimiento)
                    .fechaPago(fechaPago)            // 👈 solo si está pagada
                    .periodo(mesAProcesar)
                    .estado(estado)                  // 👈 set según pagoAprobado
                    .build();

            cuotaRepository.save(cuota);

            if (pagoAprobado) {
                log.info("✅ Cuota {} marcada como PAGADA para matriculado {} (matr. {}).",
                        cuota.getId(), m.getNombre(), m.getMatricula());
            }
        }
    }
    
    private CuotaDTO convertirADTO(Cuota cuota) {
        return CuotaDTO.builder()
                .id(cuota.getId())
                .monto(cuota.getMonto())
                .fechaVencimiento(cuota.getFechaVencimiento())
                .fechaPago(cuota.getFechaPago())
                .periodo(cuota.getPeriodo())
                .estado(cuota.getEstado())
                .nombreMatriculado(cuota.getMatriculado().getNombre())
                .matricula(cuota.getMatriculado().getMatricula())
                .build();
    }
}
