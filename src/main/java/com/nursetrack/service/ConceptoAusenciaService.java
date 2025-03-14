package com.nursetrack.service;
import com.nursetrack.model.ConceptoAusencia;
import java.util.List;
import java.util.Optional;

public interface ConceptoAusenciaService {

    List<ConceptoAusencia> findAll();

    List<ConceptoAusencia> findAllActivos();

    Optional<ConceptoAusencia> findById(Long id);

    ConceptoAusencia findByNombre(String nombre);

    ConceptoAusencia save(ConceptoAusencia conceptoAusencia);

    void deleteById(Long id);
}

