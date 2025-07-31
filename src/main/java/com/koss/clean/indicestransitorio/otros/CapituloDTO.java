package com.koss.clean.indicestransitorio.otros;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class CapituloDTO extends IndiceDTO{

    protected CapituloDTO(Integer idSeccion,String titulo, String contexto, Integer indentacion) {
        super();
    }

}
