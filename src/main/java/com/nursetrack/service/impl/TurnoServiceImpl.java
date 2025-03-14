package com.nursetrack.service.impl;


import com.nursetrack.model.Turno;
import com.nursetrack.repository.TurnoRepository;
import com.nursetrack.service.TurnoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TurnoServiceImpl implements TurnoService {

    private final TurnoRepository turnoRepository;

    public TurnoServiceImpl(TurnoRepository turnoRepository) {
        this.turnoRepository = turnoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findAll() {
        return turnoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Turno> findAllActivos() {
        return turnoRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Turno> findById(Long id) {
        return turnoRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Turno findByNombre(String nombre) {
        return turnoRepository.findByNombre(nombre);
    }

    @Override
    @Transactional
    public Turno save(Turno turno) {
        return turnoRepository.save(turno);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<Turno> optionalTurno = turnoRepository.findById(id);
        if (optionalTurno.isPresent()) {
            Turno turno = optionalTurno.get();
            turno.setActivo(false);
            turnoRepository.save(turno);
        }
    }
}