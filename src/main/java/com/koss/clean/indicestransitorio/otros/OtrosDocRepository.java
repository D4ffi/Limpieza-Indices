package com.koss.clean.indicestransitorio.otros;

import com.koss.clean.indicestransitorio.common.Indice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OtrosDocRepository extends JpaRepository<Indice, Integer> {

    /**
     * Buscar en la base de datos documentos en donde por lo menos un indice no Tesis o Trasitorio
     * Este mal y registrar ese documento y la cantidad de indices que estan mal
     */
    @Query(value = """
             SELECT DISTINCT s.id_documento\s
             FROM indice i
             INNER JOIN secciones s ON i.id_seccion = s.id_seccion
             WHERE (i.contexto LIKE '%<b>Capitulo%</b>%'\s
                    OR i.contexto LIKE '%<b>capitulo%</b>%'
                    OR i.contexto LIKE '%<b>Titulo%</b>%'
                    OR i.contexto LIKE '%<b>titulo%</b>%')
               AND s.id_documento IN (
                   SELECT d.id_documento\s
                   FROM documentos d\s
                   WHERE d.tipo_documento != 'Transitorio'\s
                     AND d.tipo_documento != 'Tesis'
               )
            \s""", nativeQuery = true)
    List<Integer> findDocumentosConIndicesCapituloTituloIncorrectos();

    @Query("""
            SELECT new com.koss.clean.indicestransitorio.otros.TituloDto(s1.idSeccion, s1.titulo, s1.texto, s1.identacion)
            FROM Secciones s1
            JOIN Secciones s2 ON s2.idSeccion = s1.idSeccion + 1
                              AND s2.idDocumento = s1.idDocumento
            WHERE s1.idDocumento = :idDocumento
              AND (s1.texto LIKE '<b>titulo%</b>'\s
                   OR s1.texto LIKE '<b>título%</b>'\s
                   OR s1.texto LIKE '<b>TITULO%</b>'\s
                   OR s1.texto LIKE '<b>TÍTULO%</b>'\s
                   OR s1.texto LIKE '<b>Título%</b>')
            ORDER BY s1.idSeccion
            """)
    List<TituloDto> findTitulosEnDocumento(@Param("idDocumento") Long idDocumento);

    @Query("""
            SELECT new com.koss.clean.indicestransitorio.otros.CapituloDTO(s1.idSeccion, s1.titulo, s1.texto, s1.identacion)
            FROM Secciones s1
            WHERE s1.idDocumento = :idDocumento
              AND (s1.texto LIKE '<b>capitulo%</b>'\s
                   OR s1.texto LIKE '<b>Capitulo%</b>'\s
                   OR s1.texto LIKE '<b>CAPITULO%</b>'\s
                   OR s1.texto LIKE '<b>Capítulo%</b>')
            ORDER BY s1.idSeccion
            """)
    List<TituloDto> findCapitulosEnDocumento(@Param("idDocumento") Long idDocumento);

    @Query("""
            SELECT new com.koss.clean.indicestransitorio.otros.ArticuloDTO(s1.idSeccion, s1.titulo,s1.texto, s1.identacion)
            FROM Secciones s1
            WHERE s1.idDocumento = :idDocumento
              AND (s1.texto LIKE '<b>articulo%'\s
                   OR s1.texto LIKE '<b>Artículo%'\s
                   OR s1.texto LIKE '<b>ARTICULO%'\s
                   OR s1.texto LIKE '<b>ARTÍCULO%')
            ORDER BY s1.idSeccion
            """)
    List<ArticuloDTO> findArticulosEnDocumento(@Param("idDocumento") Long idDocumento);

}
