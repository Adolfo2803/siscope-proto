package com.nursetrack.repository;

import com.nursetrack.model.ConceptoAusencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConceptoAusenciaRepository extends JpaRepository<ConceptoAusencia, Long> {

    List<ConceptoAusencia> findByActivoTrue();

    ConceptoAusencia findByNombre(String nombre);
}