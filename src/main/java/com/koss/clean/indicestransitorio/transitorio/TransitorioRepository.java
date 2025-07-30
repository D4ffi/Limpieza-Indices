package com.koss.clean.indicestransitorio.transitorio;

import com.koss.clean.indicestransitorio.common.Indice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransitorioRepository extends JpaRepository<Indice, Integer> {

    /**
     * Buscar en la base de datos documentos en donde por lo menos un indice transitorio
     * Este mal y registrar ese documento y la cantidad de indices transitorios que estan mal
     */
    @Query(value = """
    SELECT DISTINCT s.id_documento 
    FROM indice i
    INNER JOIN secciones s ON i.id_seccion = s.id_seccion
    WHERE i.contexto NOT LIKE '<b>Transitorio%' 
      AND i.contexto NOT LIKE '<b>transitorio%'
      AND s.id_documento IN (
          SELECT d.id_documento 
          FROM documentos d 
          WHERE d.tipo_documento = 'Transitorio'
      )
    """, nativeQuery = true)
    List<Integer> findWrongIndexTransitorios();

    /**
     * Buscar dentro del documento cada titulo transitorio, indicar que indicador_transitorio es
     * Concatenar la seccion siguiente para agregar como contexto a indice
     */
    @Query("""
    SELECT new com.koss.clean.indicestransitorio.transitorio.TransitorioDTO(s1.idSeccion, s1.indicadorTransitorio, s2.texto)
    FROM Secciones s1
    JOIN Secciones s2 ON s2.idSeccion = s1.idSeccion + 1
                      AND s2.idDocumento = s1.idDocumento
    WHERE s1.idDocumento = :idDocumento\s
      AND s1.texto LIKE '<b>Transitorio%'
    ORDER BY s1.idSeccion
   \s""")
    List<TransitorioDTO> findIndicesInDocument(@Param("idDocumento") Long idDocumento);
}
