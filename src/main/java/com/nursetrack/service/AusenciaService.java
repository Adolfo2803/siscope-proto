package com.nursetrack.service;

import com.nursetrack.model.Ausencia;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AusenciaService {

    List<Ausencia> findAll();

    Optional<Ausencia> findById(Long id);

    List<Ausencia> findByEnfermeraId(Long enfermeraId);

    List<Ausencia> findByConceptoId(Long conceptoId);

    List<Ausencia> findByFecha(LocalDate fecha);

    List<Ausencia> findByRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin);

    List<Ausencia> findByServicioAndRangoDeFechas(Long servicioId, LocalDate fechaInicio, LocalDate fechaFin);

    Ausencia save(Ausencia ausencia);

    void deleteById(Long id);
}