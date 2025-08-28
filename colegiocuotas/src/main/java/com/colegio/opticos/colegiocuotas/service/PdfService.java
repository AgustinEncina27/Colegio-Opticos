package com.colegio.opticos.colegiocuotas.service;

import org.springframework.stereotype.Service;

import com.colegio.opticos.colegiocuotas.model.Cuota;
import com.colegio.opticos.colegiocuotas.repository.CuotaRepository;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            // 1) Cargar el reporte .jasper ya compilado (en src/main/resources/reportes/)
            InputStream jasper = getClass().getResourceAsStream("/reportes/comprobante.jasper");
            if (jasper == null) {
                throw new IllegalStateException("No se encontró /reportes/comprobante.jasper en el classpath");
            }

            // 2) Parámetros
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

            // 3) Logo desde el classpath (no File)
            try (InputStream logoIs = getClass().getResourceAsStream("/static/images/logo.png")) {
                if (logoIs == null) {
                    throw new IllegalStateException("No se encontró /static/images/logo.png en el classpath");
                }
                BufferedImage logo = ImageIO.read(logoIs);
                params.put("logo", logo);
            }

            // 4) Dataset de la tabla (si el reporte lo usa)
            List<Map<String, Object>> tablaDatos = new ArrayList<>();
            Map<String, Object> fila1 = new HashMap<>();
            fila1.put("concepto", "Pago de cuota " + periodoFormateado);
            fila1.put("monto", "$" + cuota.getMonto().toString());
            tablaDatos.add(fila1);

            params.put("tablaDatos", tablaDatos);

            // 5) Llenar y exportar a PDF
            JasperPrint jp = JasperFillManager.fillReport(jasper, params, new JREmptyDataSource());
            return JasperExportManager.exportReportToPdf(jp);

        } catch (Exception e) {
            throw new RuntimeException("Error generando comprobante con JasperReports", e);
        }
    }
}
