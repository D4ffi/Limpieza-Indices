package com.koss.clean.indicestransitorio.otros;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class TituloDto extends IndiceDTO {

    public TituloDto(Integer idSeccion,String titulo, String contexto, Integer indentacion) {
        this.idSeccion = idSeccion;
        this.titulo = titulo;
        this.contexto = contexto;
        this.indentacion = indentacion;
    }

}
