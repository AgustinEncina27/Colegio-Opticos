package com.colegio.opticos.colegiocuotas.service;

import org.springframework.stereotype.Service;

import com.colegio.opticos.colegiocuotas.model.Cuota;
import com.colegio.opticos.colegiocuotas.repository.CuotaRepository;


import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JREmptyDataSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.time.format.DateTimeFormatter;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PdfService {
	
    private final CuotaRepository cuotaRepository;
    
    public byte[] generarComprobante(Long id) {
        Cuota cuota = cuotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cuota no encontrada"));
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yy");

        try {
            InputStream template = getClass().getResourceAsStream("/reportes/comprobante.jrxml");
            JasperReport report = JasperCompileManager.compileReport(template);

            Map<String, Object> params = new HashMap<>();
            params.put("id", cuota.getId());
            params.put("nombre", cuota.getMatriculado().getNombre());
            params.put("matricula", cuota.getMatriculado().getMatricula());
            params.put("dni", cuota.getMatriculado().getDni());
            String periodoFormateado = String.format("%02d-%02d", cuota.getPeriodo().getMonthValue(), cuota.getPeriodo().getYear() % 100);
            params.put("periodo", periodoFormateado);
            params.put("monto", cuota.getMonto().toString());
            params.put("vencimiento", cuota.getFechaVencimiento().format(formatter));
            params.put("fechaPago", cuota.getFechaPago() != null ? cuota.getFechaPago().format(formatter) : "-");
            params.put("estado", cuota.getEstado().toString());

            // Logo como imagen
            BufferedImage logo = ImageIO.read(new File("src/main/resources/static/images/logo.png"));
            params.put("logo", logo);

            // 👇 Agregamos tablaDatos como una lista de HashMap
            List<Map<String, Object>> tablaDatos = new ArrayList<>();

            Map<String, Object> fila1 = new HashMap<>();
            fila1.put("concepto", " Pago de cuota "+periodoFormateado);
            fila1.put("monto", "$"+cuota.getMonto().toString());

            tablaDatos.add(fila1); // podés agregar más filas si querés
            
            // 👇 Importante: pasar la lista como parámetro
            params.put("tablaDatos", tablaDatos);

            // Llenar reporte sin conexión a BD
            JasperPrint jasperPrint = JasperFillManager.fillReport(report, params, new JREmptyDataSource());

            return JasperExportManager.exportReportToPdf(jasperPrint);

        } catch (Exception e) {
            throw new RuntimeException("Error generando comprobante con JasperReports", e);
        }
    }

}
