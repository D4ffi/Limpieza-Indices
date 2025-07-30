package com.koss.clean.indicestransitorio.otros;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class ArticuloDTO extends IndiceDTO {

    public ArticuloDTO(Integer idSeccion,String titulo, String contexto, Integer idSubindice, Integer indentacion) {
        super();
    }

    @Override
    public void setIndentacion(TituloDto tituloDto) {
        this.indentacion = tituloDto.getIndentacion()+2;
    }

    @Override
    public void setSubIndice(CapituloDTO capituloDto) {
        this.idSubindice = capituloDto.getIdSeccion();
    }


}
