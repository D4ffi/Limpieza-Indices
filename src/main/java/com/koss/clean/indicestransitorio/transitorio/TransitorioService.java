package com.koss.clean.indicestransitorio.transitorio;

import com.koss.clean.indicestransitorio.common.Indice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public class TransitorioService {

    private final TransitorioRepository repository;
    Logger logger = LoggerFactory.getLogger("[ Transitorio PREVIEW ]");

    TransitorioService(TransitorioRepository repository) {
        this.repository = repository;
    }

    private final Function<TransitorioDTO, String> concatenarContexto = contexto ->
            "<b>" + contexto.getIndicador_transitorio().substring(0, 1).toUpperCase()
            + contexto.getIndicador_transitorio().substring(1) + "</b> " + contexto.getTexto();

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
