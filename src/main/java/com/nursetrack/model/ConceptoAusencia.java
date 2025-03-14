package com.nursetrack.model;

import jakarta.persistence.*;
import java.util.Set;

import lombok.*;


@Entity
@Table(name = "conceptos_ausencia")
public class ConceptoAusencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    @Column(name = "dias_maximo")
    private Integer diasMaximo;

    @OneToMany(mappedBy = "conceptoAusencia")
    private Set<Ausencia> ausencias;

    private boolean activo = true;

    public ConceptoAusencia() {
    }

    public ConceptoAusencia(Long id, String nombre, String descripcion, Integer diasMaximo, Set<Ausencia> ausencias, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.diasMaximo = diasMaximo;
        this.ausencias = ausencias;
        this.activo = activo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Integer getDiasMaximo() {
        return diasMaximo;
    }

    public void setDiasMaximo(Integer diasMaximo) {
        this.diasMaximo = diasMaximo;
    }

    public Set<Ausencia> getAusencias() {
        return ausencias;
    }

    public void setAusencias(Set<Ausencia> ausencias) {
        this.ausencias = ausencias;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
