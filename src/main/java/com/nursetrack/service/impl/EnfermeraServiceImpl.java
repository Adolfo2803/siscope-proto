package com.nursetrack.service.impl;

import com.nursetrack.model.Enfermera;
import com.nursetrack.repository.EnfermeraRepository;
import com.nursetrack.service.EnfermeraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class EnfermeraServiceImpl implements EnfermeraService {

    private final EnfermeraRepository enfermeraRepository;

    public EnfermeraServiceImpl(EnfermeraRepository enfermeraRepository) {
        this.enfermeraRepository = enfermeraRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enfermera> findAll() {
        return enfermeraRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Enfermera> findById(Long id) {
        return enfermeraRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Enfermera> findByNumeroEmpleado(String numeroEmpleado) {
        return enfermeraRepository.findByNumeroEmpleado(numeroEmpleado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enfermera> search(String term) {
        return enfermeraRepository.search(term);
    }

    @Override
    @Transactional
    public Enfermera save(Enfermera enfermera) {
        if (enfermera.getId() == null) {
            enfermera.setFechaCreacion(LocalDate.now());
        }
        return enfermeraRepository.save(enfermera);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<Enfermera> optionalEnfermera = enfermeraRepository.findById(id);
        if (optionalEnfermera.isPresent()) {
            Enfermera enfermera = optionalEnfermera.get();
            enfermera.setActivo(false);
            enfermeraRepository.save(enfermera);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enfermera> findByServicioId(Long servicioId) {
        return enfermeraRepository.findByServicioIdAndActivoTrue(servicioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Enfermera> findByTurnoId(Long turnoId) {
        return enfermeraRepository.findByTurnoIdAndActivoTrue(turnoId);
    }
}