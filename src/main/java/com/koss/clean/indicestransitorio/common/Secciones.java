package com.koss.clean.indicestransitorio.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Table(name = "secciones")
@NoArgsConstructor @Getter
@Setter
public class Secciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seccion")
    private Integer idSeccion;

    @Column(name = "accion_comparacion")
    private String accionComparacion;

    @Column(name = "advertencia")
    private byte advertencia;

    @Lob
    @Column(name="dibujo")
    private byte[] dibujo;

    @Column(name="entricula")
    private byte entricula;

    @Column(name="entricula_ley")
    private String entriculaLey;

    @Column(name="estatus")
    private byte estatus;

    @Column(name="estatus_reforma")
    private byte estatusReforma;

    @Column(name="fecha_fin")
    private Date fechaFin;

    @Column(name="fecha_inicio")
    private Date fechaInicio;

    @Column(name="fecha_reforma")
    private Date fechaReforma;

    @Column(name="identacion")
    private Integer identacion;

    @Column(name="imagen")
    private byte imagen;

    @Column(name="indexable")
    private byte indexable;

    @Column(name="nivel_superior")
    private String nivelSuperior;

    @Column(name="nivel")
    private Integer nivel;

    @Column(name="identificador_nivel")
    private Integer identificadorNivel;

    @Column(name="indicador_transitorio")
    private String indicadorTransitorio;

    @Column(name="nombre_imagen")
    private String nombreImagen;

    @Column(name="orden")
    private Integer orden;

    @Column(name="orden_parrafo")
    private Integer ordenParrafo;

    @Column(name="relacionable")
    private Integer relacionable;

    @Column(name="texto")
    private String texto;

    @Column(name="tiene_relacion")
    private byte tieneRelacion;

    @Column(name="tiene_relacion_publicada")
    private byte relacionPublicada;

    @Column(name="tipo_accion_reforma")
    private String tipoAccionReforma;

    @Column(name="titulable")
    private byte titulable;

    /*Tipo de texto - Joyita*/
    @Column(name="titulo")
    private String titulo;

    @Column(name="titulo_capturado")
    private String tituloCapturado;

    @Column(name="transitorio")
    private byte transitorio;

    @Column(name="transitorio_decreto")
    private String transitorioDecreto;

    @Column(name="id_documento")
    private Integer idDocumento;

    @Column(name="id_imagen")
    private Integer idImagen;

    @Column(name="id_seccion_origen")
    private Integer idSeccionOrigen;

    @Column(name="id_seccion_base")
    private Integer idSeccionBase;

    @Column(name="id_seccion_reforma")
    private Integer idSeccionReforma;

    @Column(name="empate_manual")
    private byte empateManual;

    @Column(name = "cantidad_actualizada")
    private byte cantidadActualizada;

    @Transient
    private Integer advertenciaArticulo;

    @Transient
    private Integer cantidadEtiquetas;

    @Transient
    private Integer tieneComentarios;

    @Transient
    private Integer bolsa;

    @Transient
    private Integer cantidadDocumento;

    @Transient
    private byte relacionado = 2;

    @Transient
    private String formatName;

    @Transient
    private String statusAnexo;

    @Transient
    private String datoTransitorio;

}
