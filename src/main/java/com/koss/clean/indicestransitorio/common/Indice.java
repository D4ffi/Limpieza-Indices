package com.koss.clean.indicestransitorio.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "indice")
@NoArgsConstructor
@Getter @Setter
public class Indice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_indice")
    private Integer idIndice;

    @Column(name = "id_seccion")
    private Integer idSeccion;

    @Column(name = "contexto")
    private String contexto;

    @Column(name = "id_subindice")
    private Integer idSubindice;

    @Column(name = "identacion")
    private Integer identacion;

}
