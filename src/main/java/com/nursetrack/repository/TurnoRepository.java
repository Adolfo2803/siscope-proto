package com.nursetrack.repository;

import com.nursetrack.model.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TurnoRepository extends JpaRepository<Turno, Long> {

    List<Turno> findByActivoTrue();

    Turno findByNombre(String nombre);
}