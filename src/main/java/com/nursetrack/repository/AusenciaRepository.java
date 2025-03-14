package com.nursetrack.repository;

import com.nursetrack.model.Ausencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AusenciaRepository extends JpaRepository<Ausencia, Long> {

    List<Ausencia> findByEnfermeraId(Long enfermeraId);

    List<Ausencia> findByConceptoAusenciaId(Long conceptoId);

    @Query("SELECT a FROM Ausencia a WHERE " +
            "a.fechaInicio <= :fecha AND a.fechaFin >= :fecha")
    List<Ausencia> findByFecha(@Param("fecha") LocalDate fecha);

    @Query("SELECT a FROM Ausencia a WHERE " +
            "a.fechaInicio >= :fechaInicio AND a.fechaFin <= :fechaFin")
    List<Ausencia> findByRangoDeFechas(
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    @Query("SELECT a FROM Ausencia a JOIN a.enfermera e WHERE " +
            "e.servicio.id = :servicioId AND " +
            "a.fechaInicio >= :fechaInicio AND a.fechaFin <= :fechaFin")
    List<Ausencia> findByServicioAndRangoDeFechas(
            @Param("servicioId") Long servicioId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
}

