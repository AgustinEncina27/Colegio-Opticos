package com.colegio.opticos.colegiocuotas.model;

import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cuota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "matriculado_id")
    private Matriculado matriculado;
    
    private BigDecimal monto;
    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
    
    @Convert(converter = YearMonthAttributeConverter.class)
    private YearMonth periodo;

    @Enumerated(EnumType.STRING)
    private EstadoCuota estado;
    
    
}
