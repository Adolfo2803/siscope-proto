package com.nursetrack.repository;
import com.nursetrack.model.Enfermera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnfermeraRepository extends JpaRepository<Enfermera, Long> {

    Optional<Enfermera> findByNumeroEmpleado(String numeroEmpleado);

    List<Enfermera> findByNombreContainingIgnoreCase(String nombre);

    List<Enfermera> findByApellidoPaternoContainingIgnoreCase(String apellidoPaterno);

    @Query("SELECT e FROM Enfermera e WHERE " +
            "LOWER(e.nombre) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.apellidoPaterno) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(e.apellidoMaterno) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "e.numeroEmpleado LIKE CONCAT('%', :search, '%')")
    List<Enfermera> search(@Param("search") String search);

    List<Enfermera> findByServicioIdAndActivoTrue(Long servicioId);

    List<Enfermera> findByTurnoIdAndActivoTrue(Long turnoId);
}
