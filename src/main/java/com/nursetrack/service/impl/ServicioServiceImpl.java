package com.nursetrack.service.impl;

import com.nursetrack.model.Servicio;
import com.nursetrack.repository.ServicioRepository;
import com.nursetrack.service.ServicioService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioServiceImpl implements ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioServiceImpl(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Servicio> findAllActivos() {
        return servicioRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Servicio> findById(Long id) {
        return servicioRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Servicio findByNombre(String nombre) {
        return servicioRepository.findByNombre(nombre);
    }

    @Override
    @Transactional
    public Servicio save(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<Servicio> optionalServicio = servicioRepository.findById(id);
        if (optionalServicio.isPresent()) {
            Servicio servicio = optionalServicio.get();
            servicio.setActivo(false);
            servicioRepository.save(servicio);
        }
    }
}
