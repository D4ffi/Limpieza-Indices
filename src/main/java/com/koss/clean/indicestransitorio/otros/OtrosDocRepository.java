package com.koss.clean.indicestransitorio.otros;

import com.koss.clean.indicestransitorio.common.Indice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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




}
