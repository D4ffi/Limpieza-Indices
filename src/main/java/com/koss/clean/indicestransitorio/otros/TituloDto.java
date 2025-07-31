package com.koss.clean.indicestransitorio.otros;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public abstract class TituloDto extends IndiceDTO {
    // Constructor protegido para subclases
    protected TituloDto(Integer idSeccion, String titulo, String contexto, Integer indentacion) {
        super();
        this.idSeccion = idSeccion;
        this.titulo = titulo;
        this.contexto = contexto;
        this.indentacion = indentacion;
    }

    public void setIndentacion() {
        this.indentacion = 0; // Títulos no tienen indentación
    }
}
