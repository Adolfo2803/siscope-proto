package com.nursetrack.service;


import com.nursetrack.model.Servicio;
import java.util.List;
import java.util.Optional;

public interface ServicioService {

    List<Servicio> findAll();

    List<Servicio> findAllActivos();

    Optional<Servicio> findById(Long id);

    Servicio findByNombre(String nombre);

    Servicio save(Servicio servicio);

    void deleteById(Long id);
}