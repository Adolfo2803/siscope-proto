package com.nursetrack.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Data;


@Entity
@Table(name = "ausencias")
public class Ausencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "enfermera_id", nullable = false)
    private Enfermera enfermera;

    @ManyToOne
    @JoinColumn(name = "concepto_id", nullable = false)
    private ConceptoAusencia conceptoAusencia;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    private String observaciones;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(name = "usuario_registro", nullable = false)
    private String usuarioRegistro;

    public Ausencia() {
    }

    public Ausencia(Long id, Enfermera enfermera, ConceptoAusencia conceptoAusencia, LocalDate fechaInicio, LocalDate fechaFin, String observaciones, LocalDate fechaRegistro, String usuarioRegistro) {
        this.id = id;
        this.enfermera = enfermera;
        this.conceptoAusencia = conceptoAusencia;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.observaciones = observaciones;
        this.fechaRegistro = fechaRegistro;
        this.usuarioRegistro = usuarioRegistro;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Enfermera getEnfermera() {
        return enfermera;
    }

    public void setEnfermera(Enfermera enfermera) {
        this.enfermera = enfermera;
    }

    public ConceptoAusencia getConceptoAusencia() {
        return conceptoAusencia;
    }

    public void setConceptoAusencia(ConceptoAusencia conceptoAusencia) {
        this.conceptoAusencia = conceptoAusencia;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getUsuarioRegistro() {
        return usuarioRegistro;
    }

    public void setUsuarioRegistro(String usuarioRegistro) {
        this.usuarioRegistro = usuarioRegistro;
    }
}

