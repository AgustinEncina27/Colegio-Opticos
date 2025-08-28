package com.colegio.opticos.colegiocuotas.build;

import net.sf.jasperreports.engine.JasperCompileManager;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReportPrecompiler {

    public static void main(String[] args) throws Exception {
        // 1) Classpath que usará el compilador de Jasper
        String cp = System.getProperty("java.class.path");
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (cl instanceof URLClassLoader ucl) {
            cp = Arrays.stream(ucl.getURLs())
                    .map(URL::getFile)
                    .map(f -> {
                        try { return new File(f).getCanonicalPath(); }
                        catch (Exception e) { return f; }
                    })
                    .collect(Collectors.joining(File.pathSeparator));
        }
        System.setProperty("jasper.reports.compile.class.path", cp);
        System.setProperty("java.awt.headless", "true");

        // 2) Preferir JDT si está, si no, caer a Javac
        String compiler = "net.sf.jasperreports.engine.design.JRJdtCompiler";
        try {
            Class.forName(compiler);
        } catch (ClassNotFoundException e) {
            compiler = "net.sf.jasperreports.engine.design.JRJavacCompiler";
        }
        System.setProperty("net.sf.jasperreports.compiler.class", compiler);
        // Para depurar, podés guardar los .java generados:
        // System.setProperty("net.sf.jasperreports.compiler.keep.java.file", "true");

        // 3) Directorios
        Path src = Paths.get("src/main/resources/reportes");
        Path out = Paths.get("target/classes/reportes");
        Files.createDirectories(out);

        // 4) Compilar todos los .jrxml a .jasper
        try (Stream<Path> files = Files.walk(src)) {
            files.filter(p -> p.toString().endsWith(".jrxml"))
                 .forEach(p -> {
                    try {
                        Path dest = out.resolve(
                            p.getFileName().toString().replace(".jrxml", ".jasper")
                        );
                        JasperCompileManager.compileReportToFile(p.toString(), dest.toString()); 
                        System.out.println("✔ Compilado: " + p.getFileName() + " -> " + dest.getFileName());
                    } catch (Exception ex) {
                        throw new RuntimeException("Error compilando " + p, ex);
                    }
                 });
        }
    }
}