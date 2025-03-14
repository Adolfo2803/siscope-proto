package com.nursetrack.model;

import jakarta.persistence.*;
import java.util.Set;
import lombok.Data;


@Entity
@Table(name = "servicios")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    @OneToMany(mappedBy = "servicio")
    private Set<Enfermera> enfermeras;

    private boolean activo = true;

    public Servicio() {
    }

    public Servicio(Long id, String nombre, String descripcion, Set<Enfermera> enfermeras, boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.enfermeras = enfermeras;
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

    public Set<Enfermera> getEnfermeras() {
        return enfermeras;
    }

    public void setEnfermeras(Set<Enfermera> enfermeras) {
        this.enfermeras = enfermeras;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}
