package com.koss.clean.indicestransitorio.transitorio;

import com.koss.clean.indicestransitorio.common.Indice;
import com.koss.clean.indicestransitorio.common.SqlScriptGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class TransitorioService {

    private final TransitorioRepository repository;
    private final SqlScriptGenerator scriptGenerator;
    Logger logger = LoggerFactory.getLogger("[ Transitorio PREVIEW ]");

    public TransitorioService(TransitorioRepository repository, SqlScriptGenerator scriptGenerator) {
        this.repository = repository;
        this.scriptGenerator = scriptGenerator;
    }

    public void flux(boolean isPreview) {
        List<Integer> wrongIndices = findWrongIndexTransitorios();

        if (wrongIndices.isEmpty()) {
            logger.info("✅ No hay índices transitorios incorrectos.");
            return;
        }

        logger.info("🔍 Encontrados {} documentos con índices transitorios incorrectos", wrongIndices.size());

        if (isPreview) {
            procesarPreview(wrongIndices);
        } else {
            procesarYGenerarScript(wrongIndices);
        }
    }

    private List<Integer> findWrongIndexTransitorios() {
        return repository.findWrongIndexTransitorios();
    }

    private final Function<TransitorioDTO, String> concatenarContexto = contexto ->
            "<b>" + contexto.getIndicadorTransitorio().substring(0, 1).toUpperCase()
            + contexto.getIndicadorTransitorio().substring(1) + "</b> " + contexto.getTexto();

    public Indice buildIndex(TransitorioDTO dto) {
        Indice indice = new Indice();
        indice.setIdSeccion(dto.getIdSeccion());
        indice.setContexto(concatenarContexto.apply(dto));
        indice.setIdSubindice(null); // Transitorio no tiene subíndices
        indice.setIdentacion(0); // Transitorio no tiene indentación
        return indice;
    }

    public void save(Indice indice) {
        if (indice != null && indice.getIdSeccion() != null) {
            repository.save(indice);
        } else {
            throw new IllegalArgumentException("Indice o id de seccion no puede ser nulo");
        }
    }

    /**
     * =================================
     *          Preview index
     * =================================
     */

    public void showEditedIndex(TransitorioDTO dto) {
        Indice indice = buildIndex(dto);
        logger.info("""
            Preview de índice transitorio:\s
            ID Sección: {}
            Contexto: {}
            ID Subíndice: {}
            Indentación: {}""",
                indice.getIdSeccion(),
                indice.getContexto(),
                indice.getIdSubindice(),
                indice.getIdentacion());
    }

    private void procesarPreview(List<Integer> wrongIndices) {
        logger.info("=== MODO PREVIEW - ÍNDICES TRANSITORIOS ===");

        wrongIndices.forEach(idDocumento -> {
            List<TransitorioDTO> transitorios = repository.findIndicesInDocument(idDocumento.longValue());

            if (!transitorios.isEmpty()) {
                logger.info("📄 Documento ID: {} - {} índices a corregir", idDocumento, transitorios.size());
                transitorios.forEach(this::showEditedIndex);
                logger.info("────────────────────────────────────────");
            }
        });

        int totalIndices = wrongIndices.stream()
                .mapToInt(id -> repository.findIndicesInDocument(id.longValue()).size())
                .sum();

        logger.info("📊 RESUMEN: {} documentos afectados, {} índices a corregir en total",
                wrongIndices.size(), totalIndices);
    }

    private void procesarYGenerarScript(List<Integer> wrongIndices) {
        logger.info("=== MODO EJECUCIÓN - GENERANDO SCRIPT SQL ===");

        List<TransitorioDTO> todosLosTransitorios = new ArrayList<>();

        wrongIndices.forEach(idDocumento -> {
            List<TransitorioDTO> transitorios = repository.findIndicesInDocument(idDocumento.longValue());

            if (!transitorios.isEmpty()) {
                logger.info("📄 Procesando documento ID: {} - {} índices", idDocumento, transitorios.size());
                todosLosTransitorios.addAll(transitorios);

                // Generar script individual por documento
                String scriptFile = scriptGenerator.generateTransitorioScript(transitorios, idDocumento);
                logger.info("📝 Script individual generado: {}", scriptFile);
            }
        });

        // Generar script batch con todos los documentos
        if (!todosLosTransitorios.isEmpty()) {
            String batchScript = scriptGenerator.generateBatchTransitorioScript(
                    wrongIndices,
                    id -> repository.findIndicesInDocument(id.longValue())
            );
            logger.info("📦 Script batch generado: {}", batchScript);
        }

        logger.info("✅ Procesamiento completado. Scripts SQL generados.");
    }

}
