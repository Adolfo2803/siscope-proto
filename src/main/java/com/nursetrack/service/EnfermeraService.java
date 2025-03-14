package com.nursetrack.service;

import com.nursetrack.model.Enfermera;
import java.util.List;
import java.util.Optional;

public interface EnfermeraService {

    List<Enfermera> findAll();

    Optional<Enfermera> findById(Long id);

    Optional<Enfermera> findByNumeroEmpleado(String numeroEmpleado);

    List<Enfermera> search(String term);

    Enfermera save(Enfermera enfermera);

    void deleteById(Long id);

    List<Enfermera> findByServicioId(Long servicioId);

    List<Enfermera> findByTurnoId(Long turnoId);
}