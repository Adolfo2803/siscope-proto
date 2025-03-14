package com.nursetrack.service.impl;

import com.nursetrack.model.ConceptoAusencia;
import com.nursetrack.repository.ConceptoAusenciaRepository;
import com.nursetrack.service.ConceptoAusenciaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConceptoAusenciaServiceImpl implements ConceptoAusenciaService {

    private final ConceptoAusenciaRepository conceptoAusenciaRepository;

    public ConceptoAusenciaServiceImpl(ConceptoAusenciaRepository conceptoAusenciaRepository) {
        this.conceptoAusenciaRepository = conceptoAusenciaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConceptoAusencia> findAll() {
        return conceptoAusenciaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ConceptoAusencia> findAllActivos() {
        return conceptoAusenciaRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConceptoAusencia> findById(Long id) {
        return conceptoAusenciaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public ConceptoAusencia findByNombre(String nombre) {
        return conceptoAusenciaRepository.findByNombre(nombre);
    }

    @Override
    @Transactional
    public ConceptoAusencia save(ConceptoAusencia conceptoAusencia) {
        return conceptoAusenciaRepository.save(conceptoAusencia);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Optional<ConceptoAusencia> optionalConcepto = conceptoAusenciaRepository.findById(id);
        if (optionalConcepto.isPresent()) {
            ConceptoAusencia concepto = optionalConcepto.get();
            concepto.setActivo(false);
            conceptoAusenciaRepository.save(concepto);
        }
    }
}
