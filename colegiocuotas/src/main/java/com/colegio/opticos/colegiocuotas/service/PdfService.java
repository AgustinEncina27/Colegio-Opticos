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
            // ⬇️ cargar el .jasper ya compilado
            InputStream jasper = getClass().getResourceAsStream("/reportes/comprobante.jasper");
            if (jasper == null) throw new IllegalStateException("No se encontró /reportes/comprobante.jasper");

            Map<String, Object> params = new HashMap<>();
            params.put("id", cuota.getId());
            params.put("nombre", cuota.getMatriculado().getNombre());
            params.put("matricula", cuota.getMatriculado().getMatricula());
            params.put("dni", cuota.getMatriculado().getDni());
            String periodoFormateado = String.format("%02d-%02d",
                    cuota.getPeriodo().getMonthValue(), cuota.getPeriodo().getYear() % 100);
            params.put("periodo", periodoFormateado);
            params.put("monto", cuota.getMonto().toString());
            params.put("vencimiento", cuota.getFechaVencimiento().format(formatter));
            params.put("fechaPago", cuota.getFechaPago() != null ? cuota.getFechaPago().format(formatter) : "-");
            params.put("estado", cuota.getEstado().toString());

            // ⬇️ logo desde el classpath (no File)
            try (InputStream logoIs = getClass().getResourceAsStream("/static/images/logo.png")) {
                if (logoIs == null) throw new IllegalStateException("No se encontró /static/images/logo.png");
                BufferedImage logo = ImageIO.read(logoIs);
                params.put("logo", logo);
            }

            JasperPrint jp = JasperFillManager.fillReport(jasper, params, new JREmptyDataSource());
            return JasperExportManager.exportReportToPdf(jp);

        } catch (Exception e) {
            throw new RuntimeException("Error generando comprobante con JasperReports", e);
        }
    }

}
