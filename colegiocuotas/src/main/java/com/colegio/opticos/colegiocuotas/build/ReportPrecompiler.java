package com.colegio.opticos.colegiocuotas.build;

import net.sf.jasperreports.engine.JasperCompileManager;

import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public class ReportPrecompiler {
    public static void main(String[] args) throws Exception {
        Path src = Paths.get("src/main/resources/reportes");
        Path out = Paths.get("target/classes/reportes"); // entra al JAR

        if (!Files.exists(src)) {
            System.out.println("No hay carpeta de reportes en " + src.toAbsolutePath());
            return;
        }
        Files.createDirectories(out);

        try (Stream<Path> paths = Files.walk(src)) {
            paths.filter(p -> p.toString().endsWith(".jrxml")).forEach(p -> {
                try {
                    Path rel = src.relativize(p); // conserva subcarpetas
                    Path target = out.resolve(rel.toString().replace(".jrxml", ".jasper"));
                    Files.createDirectories(target.getParent());
                    JasperCompileManager.compileReportToFile(p.toString(), target.toString());
                    System.out.println("✔ Compilado: " + p + " → " + target);
                } catch (Exception e) {
                    throw new RuntimeException("Error compilando " + p, e);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("✅ Reportes compilados en: " + out.toAbsolutePath());
    }
}
