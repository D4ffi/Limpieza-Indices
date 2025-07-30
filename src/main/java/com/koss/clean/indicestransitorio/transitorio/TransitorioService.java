package com.koss.clean.indicestransitorio.transitorio;

import com.koss.clean.indicestransitorio.common.Indice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TransitorioService {

    private final TransitorioRepository repository;
    Logger logger = LoggerFactory.getLogger("[ Transitorio PREVIEW ]");

    TransitorioService(TransitorioRepository repository) {
        this.repository = repository;
    }

    public void flux(boolean ispreview){
        List<Integer> wrongIndices = findWrongIndexTransitorios();
        if (wrongIndices.isEmpty()) {
            logger.info("No hay indices transitorios incorrectos.");
        }
            wrongIndices.forEach(id -> {
                List<TransitorioDTO> transitorios = repository.findIndicesInDocument(id.longValue());
                List<Indice> indices = new ArrayList<>();
                if (ispreview){
                    transitorios.forEach(this::showEditedIndex);
                } else {
                    transitorios.forEach(dto -> indices.add(buildIndex(dto)));
                    indices.forEach(this::save);
                }
            });
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

}
