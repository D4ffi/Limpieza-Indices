package com.koss.clean.indicestransitorio.otros;

import com.koss.clean.indicestransitorio.common.Indice;
import com.koss.clean.indicestransitorio.common.SqlScriptGenerator;
import com.koss.clean.indicestransitorio.transitorio.TransitorioDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

@Service
public class OtrosDocService {

    private final OtrosDocRepository otrosDocRepository;
    private final SqlScriptGenerator sqlScriptGenerator;
    private Integer idSeccionTitulo = 0;

    public OtrosDocService(OtrosDocRepository otrosDocRepository, SqlScriptGenerator sqlScriptGenerator) {
        this.otrosDocRepository = otrosDocRepository;
        this.sqlScriptGenerator = sqlScriptGenerator;
    }

    public void flux(boolean isPreview) {
        List<Integer> wrongIndices = findWrongIndexCapituloTitulo();

        if (wrongIndices.isEmpty()) {
            System.out.println("✅ No hay índices de capítulo o título incorrectos.");
            return;
        }

        System.out.println("🔍 Encontrados " + wrongIndices.size() + " documentos con índices de capítulo o título incorrectos");

        if (isPreview) {
            procesarPreview(wrongIndices);
        } else {
            procesarYGenerarScript(wrongIndices);
        }
    }

    private List<Integer> findWrongIndexCapituloTitulo() {
        return otrosDocRepository.findDocumentosConIndicesCapituloTituloIncorrectos();
    }

    private final Function<TituloDto, String> concatenarContexto = contexto ->
            "<b>" + contexto.getTitulo().substring(0, 1).toUpperCase()
            + contexto.getTitulo().substring(1) + "</b> " + contexto.getContexto();

    private final Function<CapituloDTO, String> concatenarContextoCap = contexto ->
            "<b>" + contexto.getTitulo().substring(0, 1).toUpperCase()
            + contexto.getTitulo().substring(1) + "</b> " + contexto.getContexto();

    private final Function<ArticuloDTO, String> concatenarContextoArt = contexto ->
            "<b>" + contexto.getTitulo().substring(0, 1).toUpperCase()
            + contexto.getTitulo().substring(1) + "</b> " + contexto.getContexto();

    private final Function<Object, Void> identarIndices = dto -> {
        if (dto instanceof TituloDto) {
            ((TituloDto) dto).setIndentacion();
        } else if (dto instanceof CapituloDTO) {
            ((CapituloDTO) dto).setIndentacion(1);
        } else if (dto instanceof ArticuloDTO) {
            ((ArticuloDTO) dto).setIndentacion(2);
        }
        return null;
    };

    public Indice buildIndex(Object dto) {
        Indice indice = new Indice();
        if (dto instanceof TituloDto tituloDto) {
            idSeccionTitulo = tituloDto.getIdSeccion();
            indice.setIdSeccion(tituloDto.getIdSeccion());
            indice.setContexto(concatenarContexto.apply(tituloDto));
            indice.setIdSubindice(null); // Transitorio no tiene subíndices
            indice.setIdentacion(0);
        } else if (dto instanceof CapituloDTO capituloDTO) {
            indice.setIdSeccion(capituloDTO.getIdSeccion());
            indice.setContexto(concatenarContextoCap.apply(capituloDTO));
            indice.setIdSubindice(idSeccionTitulo);
            idSeccionTitulo = capituloDTO.getIdSeccion();
        } else if (dto instanceof ArticuloDTO articuloDTO) {
            indice.setIdSeccion(articuloDTO.getIdSeccion());
            indice.setContexto(concatenarContextoArt.apply(articuloDTO));
            indice.setIdSubindice(idSeccionTitulo);
        } else {
            throw new IllegalArgumentException("Tipo de DTO no soportado: " + dto.getClass().getName());
        }
        return indice;
    }
}
