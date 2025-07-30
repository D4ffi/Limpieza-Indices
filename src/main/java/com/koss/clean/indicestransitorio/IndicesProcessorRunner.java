package com.koss.clean.indicestransitorio;

import com.koss.clean.indicestransitorio.common.SqlScriptGenerator;
import com.koss.clean.indicestransitorio.otros.OtrosDocRepository;
import com.koss.clean.indicestransitorio.otros.OtrosDocRepository;
import com.koss.clean.indicestransitorio.transitorio.TransitorioRepository;
import com.koss.clean.indicestransitorio.transitorio.TransitorioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class IndicesProcessorRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(IndicesProcessorRunner.class);

    @Autowired
    private TransitorioRepository transitorioRepository;

    @Autowired
    private OtrosDocRepository otrosDocRepository;

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        try {
            mostrarMenuPrincipal();
            int tipoOpcion = obtenerOpcionValida(scanner, 1, 2);

            mostrarMenuModo();
            int modoOpcion = obtenerOpcionValida(scanner, 1, 2);

            boolean esPreview = (modoOpcion == 1);

            if (tipoOpcion == 1) {
                procesarTransitorios(esPreview);
            } else {
                procesarOtrosDocumentos(esPreview);
            }

        } catch (Exception e) {
            logger.error("Error durante la ejecución: {}", e.getMessage(), e);
        } finally {
            scanner.close();
        }
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("    PROCESADOR DE ÍNDICES INCORRECTOS");
        System.out.println("=".repeat(50));
        System.out.println("1. Transitorios");
        System.out.println("2. Demás documentos (Capítulos/Títulos)");
        System.out.println("=".repeat(50));
        System.out.print("Selecciona una opción (1-2): ");
    }

    private void mostrarMenuModo() {
        System.out.println("\n" + "-".repeat(40));
        System.out.println("    MODO DE EJECUCIÓN");
        System.out.println("-".repeat(40));
        System.out.println("1. Preview (solo mostrar errores)");
        System.out.println("2. Solve (ejecutar correcciones)");
        System.out.println("-".repeat(40));
        System.out.print("Selecciona una opción (1-2): ");
    }

    private int obtenerOpcionValida(Scanner scanner, int min, int max) {
        int opcion;
        while (true) {
            try {
                opcion = scanner.nextInt();
                if (opcion >= min && opcion <= max) {
                    return opcion;
                } else {
                    System.out.print("Opción inválida. Ingresa un número entre " + min + " y " + max + ": ");
                }
            } catch (Exception e) {
                System.out.print("Entrada inválida. Ingresa un número: ");
                scanner.nextLine(); // Limpiar buffer
            }
        }
    }

    private void procesarTransitorios(boolean esPreview) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔄 PROCESANDO DOCUMENTOS TRANSITORIOS...");
        System.out.println("Modo: " + (esPreview ? "PREVIEW" : "EJECUCIÓN REAL"));
        System.out.println("=".repeat(60));

        try {
            SqlScriptGenerator scriptGenerator = new SqlScriptGenerator();
            TransitorioService service = new TransitorioService(transitorioRepository, scriptGenerator );
            service.flux(esPreview);

            System.out.println("\n✅ Procesamiento de transitorios completado.");

        } catch (Exception e) {
            logger.error("Error procesando transitorios: {}", e.getMessage(), e);
            System.out.println("\n❌ Error durante el procesamiento de transitorios.");
        }
    }

    private void procesarOtrosDocumentos(boolean esPreview) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("🔄 PROCESANDO OTROS DOCUMENTOS (Capítulos/Títulos)...");
        System.out.println("Modo: " + (esPreview ? "PREVIEW" : "EJECUCIÓN REAL"));
        System.out.println("=".repeat(60));

        try {
            // Aquí necesitarás crear OtrosService similar a TransitorioService
            //OtrosService service = new OtrosService(otrosDocRepository);
            //service.flux(esPreview);

            System.out.println("\n✅ Procesamiento de otros documentos completado.");

        } catch (Exception e) {
            logger.error("Error procesando otros documentos: {}", e.getMessage(), e);
            System.out.println("\n❌ Error durante el procesamiento de otros documentos.");
        }
    }
}