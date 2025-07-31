package com.koss.clean.indicestransitorio.otros;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class ArticuloDTO extends IndiceDTO {

    protected ArticuloDTO(Integer idSeccion,String titulo, String contexto, Integer indentacion) {
        super();
    }

}
