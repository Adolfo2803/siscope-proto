package com.nursetrack.service;

import com.nursetrack.model.Turno;
import java.util.List;
import java.util.Optional;

public interface TurnoService {

    List<Turno> findAll();

    List<Turno> findAllActivos();

    Optional<Turno> findById(Long id);

    Turno findByNombre(String nombre);

    Turno save(Turno turno);

    void deleteById(Long id);
}
