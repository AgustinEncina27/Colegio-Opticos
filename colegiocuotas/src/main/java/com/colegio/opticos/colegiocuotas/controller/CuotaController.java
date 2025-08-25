package com.colegio.opticos.colegiocuotas.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.colegio.opticos.colegiocuotas.dto.CuotaDTO;
import com.colegio.opticos.colegiocuotas.dto.MatriculadoDTO;
import com.colegio.opticos.colegiocuotas.service.CuotaService;
import com.colegio.opticos.colegiocuotas.service.PdfService;

import java.util.List;

import org.springframework.data.domain.Page;


@RestController
@RequestMapping("/api/cuotas")
@RequiredArgsConstructor
public class CuotaController {

    private final CuotaService cuotaService;
    
    private final PdfService pdfService;

    @GetMapping("/mis-cuotas")
    public ResponseEntity<Page<CuotaDTO>> getMisCuotas(
    		Authentication auth,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Page<CuotaDTO> cuotas = cuotaService.obtenerCuotasPaginadas(auth.getName(),page, size);
        return ResponseEntity.ok(cuotas);
    }
    
    @GetMapping("/pagos-filtrados")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<CuotaDTO>> listarCuotasParaAdmin(
            @RequestParam(defaultValue = "") String filtro,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(cuotaService.listarCuotasParaAdmin(filtro, page, size));
    }
    
    
    
    @GetMapping("/{id}/comprobante")
    public ResponseEntity<byte[]> descargarComprobante(@PathVariable Long id) {
        byte[] pdf = pdfService.generarComprobante(id);
        
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=comprobante-cuota-" + id + ".pdf")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(pdf);
    }
    
    @PatchMapping("/pagar-multiple")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> marcarMultiplesCuotasPagadas(@RequestBody List<Long> ids) {
        ids.forEach(cuotaService::marcarComoPagada);
        return ResponseEntity.ok().build();
    }
    
    @PatchMapping("/{id}/revertir-pago")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> revertirPago(@PathVariable Long id) {
        cuotaService.revertirPago(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/generar-cuotas")
    public ResponseEntity<String> generarCuotas() {
        cuotaService.generarCuotasMensuales();
        return ResponseEntity.ok("Cuotas generadas manualmente");
    }
    
    @GetMapping("/morosos")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<MatriculadoDTO>> getMorosos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(cuotaService.obtenerMatriculadosMorosos(page, size));
    }
    
}