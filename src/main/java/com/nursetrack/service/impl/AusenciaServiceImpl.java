package com.nursetrack.service.impl;

import com.nursetrack.model.Ausencia;
import com.nursetrack.repository.AusenciaRepository;
import com.nursetrack.service.AusenciaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AusenciaServiceImpl implements AusenciaService {

    private final AusenciaRepository ausenciaRepository;

    public AusenciaServiceImpl(AusenciaRepository ausenciaRepository) {
        this.ausenciaRepository = ausenciaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ausencia> findAll() {
        return ausenciaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ausencia> findById(Long id) {
        return ausenciaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ausencia> findByEnfermeraId(Long enfermeraId) {
        return ausenciaRepository.findByEnfermeraId(enfermeraId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ausencia> findByConceptoId(Long conceptoId) {
        return ausenciaRepository.findByConceptoAusenciaId(conceptoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ausencia> findByFecha(LocalDate fecha) {
        return ausenciaRepository.findByFecha(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ausencia> findByRangoDeFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return ausenciaRepository.findByRangoDeFechas(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Ausencia> findByServicioAndRangoDeFechas(Long servicioId, LocalDate fechaInicio, LocalDate fechaFin) {
        return ausenciaRepository.findByServicioAndRangoDeFechas(servicioId, fechaInicio, fechaFin);
    }

    @Override
    @Transactional
    public Ausencia save(Ausencia ausencia) {
        if (ausencia.getFechaRegistro() == null) {
            ausencia.setFechaRegistro(LocalDate.now());
        }
        return ausenciaRepository.save(ausencia);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        ausenciaRepository.deleteById(id);
    }
}
