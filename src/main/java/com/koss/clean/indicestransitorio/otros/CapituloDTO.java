package com.koss.clean.indicestransitorio.otros;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class CapituloDTO extends IndiceDTO{

    public CapituloDTO(Integer idSeccion, String contexto, Integer indentacion) {
        super();
    }

    @Override
    public void setIndentacion(TituloDto tituloDto) {
        this.indentacion = tituloDto.getIndentacion()+1;
    }

    @Override
    public void setSubIndice(TituloDto tituloDto) {
        this.idSubindice = tituloDto.getIdSeccion();
    }
}
