package com.koss.clean.indicestransitorio.otros;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter @Setter
public abstract class IndiceDTO {

    Integer idSeccion;
    String titulo;
    String contexto;
    Integer idSubindice;
    Integer indentacion;

    public abstract void setIndentacion(TituloDto tituloDto);
    public abstract void setSubIndice(TituloDto tituloDto);
    public abstract void setSubIndice(CapituloDTO capituloDto);
}
