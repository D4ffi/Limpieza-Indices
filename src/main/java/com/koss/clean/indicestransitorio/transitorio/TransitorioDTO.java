package com.koss.clean.indicestransitorio.transitorio;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TransitorioDTO {

    private Integer idSeccion;
    private String indicadorTransitorio;
    private String texto;

    public TransitorioDTO(Integer idSeccion,String indicadorTransitorio, String texto) {
        this.idSeccion = idSeccion;
        this.indicadorTransitorio = indicadorTransitorio;
        this.texto = texto;
    }


}
