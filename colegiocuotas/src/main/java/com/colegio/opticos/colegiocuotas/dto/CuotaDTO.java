package com.colegio.opticos.colegiocuotas.dto;

import com.colegio.opticos.colegiocuotas.model.EstadoCuota;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CuotaDTO {
    private Long id;
    private BigDecimal monto;
    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
    private YearMonth periodo;
    private EstadoCuota estado;
    private String nombreMatriculado;
    private Integer matricula;
}
