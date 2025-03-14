package com.nursetrack.service;

import com.nursetrack.model.Ausencia;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ReporteService {

    List<Ausencia> generarReporteAusencias(LocalDate fechaInicio, LocalDate fechaFin);

    List<Ausencia> generarReporteAusenciasPorServicio(Long servicioId, LocalDate fechaInicio, LocalDate fechaFin);

    List<Ausencia> generarReporteAusenciasPorTurno(Long turnoId, LocalDate fechaInicio, LocalDate fechaFin);

    List<Ausencia> generarReporteAusenciasPorConcepto(Long conceptoId, LocalDate fechaInicio, LocalDate fechaFin);

    Map<String, Long> getEstadisticasPorConcepto(LocalDate fechaInicio, LocalDate fechaFin);

    Map<String, Long> getEstadisticasPorServicio(LocalDate fechaInicio, LocalDate fechaFin);

    byte[] exportarReporteExcel(List<Ausencia> ausencias);

    byte[] exportarReportePdf(List<Ausencia> ausencias);
}
