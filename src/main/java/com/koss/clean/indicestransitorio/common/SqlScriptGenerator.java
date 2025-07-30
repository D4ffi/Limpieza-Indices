package com.koss.clean.indicestransitorio.common;

import com.koss.clean.indicestransitorio.transitorio.TransitorioDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

@Component
public class SqlScriptGenerator {

    private static final Logger logger = LoggerFactory.getLogger(SqlScriptGenerator.class);
    private static final String SCRIPT_DIRECTORY = "scripts/";

    private final Function<TransitorioDTO, String> concatenarContexto = contexto ->
            "<b>" + contexto.getIndicadorTransitorio().substring(0, 1).toUpperCase()
            + contexto.getIndicadorTransitorio().substring(1) + "</b> " + contexto.getTexto();

    /**
     * Genera un archivo SQL con INSERT statements para índices transitorios
     */
    public String generateTransitorioScript(List<TransitorioDTO> transitorios, Integer idDocumento) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("%stransitorio_indices_doc_%d_%s.sql", SCRIPT_DIRECTORY, idDocumento, timestamp);

        try {
            // Crear directorio si no existe
            createDirectoryIfNotExists();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writeScriptHeader(writer, idDocumento, transitorios.size());
                writeTransitorioInserts(writer, transitorios);
                writeScriptFooter(writer);
            }

            logger.info("✅ Script SQL generado: {}", fileName);
            return fileName;

        } catch (IOException e) {
            logger.error("❌ Error generando script SQL: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar script SQL", e);
        }
    }

    /**
     * Genera un archivo SQL con INSERT statements para índices de otros documentos
     */
    public String generateOtrosDocScript(List<Indice> indices, String tipoDocumento) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("%sotros_docs_indices_%s_%s.sql", SCRIPT_DIRECTORY, tipoDocumento.toLowerCase(), timestamp);

        try {
            createDirectoryIfNotExists();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writeScriptHeader(writer, tipoDocumento, indices.size());
                writeIndicesInserts(writer, indices);
                writeScriptFooter(writer);
            }

            logger.info("✅ Script SQL generado: {}", fileName);
            return fileName;

        } catch (IOException e) {
            logger.error("❌ Error generando script SQL: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar script SQL", e);
        }
    }

    private void createDirectoryIfNotExists() throws IOException {
        java.nio.file.Path directory = java.nio.file.Paths.get(SCRIPT_DIRECTORY);
        if (!java.nio.file.Files.exists(directory)) {
            java.nio.file.Files.createDirectories(directory);
            logger.info("📁 Directorio creado: {}", SCRIPT_DIRECTORY);
        }
    }

    private void writeScriptHeader(BufferedWriter writer, Object identifier, int totalRecords) throws IOException {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        writer.write("-- ========================================\n");
        writer.write("-- SCRIPT DE CORRECCIÓN DE ÍNDICES\n");
        writer.write("-- ========================================\n");
        writer.write(String.format("-- Generado: %s\n", timestamp));
        writer.write(String.format("-- Identificador: %s\n", identifier));
        writer.write(String.format("-- Total de registros: %d\n", totalRecords));
        writer.write("-- ========================================\n\n");

        writer.write("-- Iniciar transacción\n");
        writer.write("START TRANSACTION;\n\n");

        writer.write("-- Configurar modo SQL\n");
        writer.write("SET sql_mode = 'STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE,ERROR_FOR_DIVISION_BY_ZERO';\n\n");
    }

    private void writeTransitorioInserts(BufferedWriter writer, List<TransitorioDTO> transitorios) throws IOException {
        writer.write("-- INSERT statements para índices transitorios\n");
        writer.write("-- ==========================================\n\n");

        for (TransitorioDTO dto : transitorios) {
            String contexto = concatenarContexto.apply(dto);
            String escapedContexto = escapeSqlString(contexto);

            writer.write(String.format(
                    "INSERT INTO indice (id_seccion, contexto, id_subindice, identacion) VALUES (%d, '%s', NULL, 0);\n",
                    dto.getIdSeccion(),
                    escapedContexto
            ));
        }
        writer.write("\n");
    }

    private void writeIndicesInserts(BufferedWriter writer, List<Indice> indices) throws IOException {
        writer.write("-- INSERT statements para índices\n");
        writer.write("-- ==============================\n\n");

        for (Indice indice : indices) {
            String escapedContexto = escapeSqlString(indice.getContexto());

            writer.write(String.format(
                    "INSERT INTO indice (id_seccion, contexto, id_subindice, identacion) VALUES (%d, '%s', %s, %d);\n",
                    indice.getIdSeccion(),
                    escapedContexto,
                    indice.getIdSubindice() != null ? indice.getIdSubindice().toString() : "NULL",
                    indice.getIdentacion() != null ? indice.getIdentacion() : 0
            ));
        }
        writer.write("\n");
    }

    private void writeScriptFooter(BufferedWriter writer) throws IOException {
        writer.write("-- ========================================\n");
        writer.write("-- FINALIZAR TRANSACCIÓN\n");
        writer.write("-- ========================================\n\n");

        writer.write("-- Verificar cambios antes de confirmar\n");
        writer.write("-- SELECT COUNT(*) FROM indice WHERE id_seccion IN (/* IDs de secciones modificadas */);\n\n");

        writer.write("-- Confirmar cambios (descomenta la siguiente línea cuando estés seguro)\n");
        writer.write("-- COMMIT;\n\n");

        writer.write("-- En caso de error, deshacer cambios (descomenta la siguiente línea)\n");
        writer.write("-- ROLLBACK;\n\n");

        writer.write("-- Script generado por IndicesTransitorioApplication\n");
        writer.write(String.format("-- Fecha: %s\n",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
    }

    /**
     * Escapa caracteres especiales en strings SQL
     */
    private String escapeSqlString(String input) {
        if (input == null) {
            return "";
        }
        return input
                .replace("'", "''")           // Escapar comillas simples
                .replace("\\", "\\\\")        // Escapar backslashes
                .replace("\n", "\\n")         // Escapar saltos de línea
                .replace("\r", "\\r")         // Escapar retornos de carro
                .replace("\t", "\\t");        // Escapar tabs
    }

    /**
     * Genera un script completo con múltiples documentos
     */
    public String generateBatchTransitorioScript(List<Integer> documentIds,
                                                 Function<Integer, List<TransitorioDTO>> dataProvider) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = String.format("%sbatch_transitorio_indices_%s.sql", SCRIPT_DIRECTORY, timestamp);

        try {
            createDirectoryIfNotExists();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                writeScriptHeader(writer, "BATCH_TRANSITORIOS", documentIds.size());

                for (Integer docId : documentIds) {
                    List<TransitorioDTO> transitorios = dataProvider.apply(docId);
                    if (!transitorios.isEmpty()) {
                        writer.write(String.format("-- Documento ID: %d (%d registros)\n", docId, transitorios.size()));
                        writer.write("-- " + "-".repeat(40) + "\n");
                        writeTransitorioInserts(writer, transitorios);
                    }
                }

                writeScriptFooter(writer);
            }

            logger.info("✅ Script SQL batch generado: {}", fileName);
            return fileName;

        } catch (IOException e) {
            logger.error("❌ Error generando script SQL batch: {}", e.getMessage(), e);
            throw new RuntimeException("Error al generar script SQL batch", e);
        }
    }
}