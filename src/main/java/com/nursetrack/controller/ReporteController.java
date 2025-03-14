package com.nursetrack.controller;


import com.nursetrack.model.Ausencia;
import com.nursetrack.model.Servicio;
import com.nursetrack.model.Turno;
import com.nursetrack.model.ConceptoAusencia;
import com.nursetrack.service.ReporteService;
import com.nursetrack.service.ServicioService;
import com.nursetrack.service.TurnoService;
import com.nursetrack.service.ConceptoAusenciaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/reporte")
public class ReporteController {

    private final ReporteService reporteService;
    private final ServicioService servicioService;
    private final TurnoService turnoService;
    private final ConceptoAusenciaService conceptoAusenciaService;

    public ReporteController(ReporteService reporteService,
                             ServicioService servicioService,
                             TurnoService turnoService,
                             ConceptoAusenciaService conceptoAusenciaService) {
        this.reporteService = reporteService;
        this.servicioService = servicioService;
        this.turnoService = turnoService;
        this.conceptoAusenciaService = conceptoAusenciaService;
    }

    @GetMapping
    public String mostrarFormularioReporte(Model model) {
        List<Servicio> servicios = servicioService.findAllActivos();
        List<Turno> turnos = turnoService.findAllActivos();
        List<ConceptoAusencia> conceptos = conceptoAusenciaService.findAllActivos();

        model.addAttribute("servicios", servicios);
        model.addAttribute("turnos", turnos);
        model.addAttribute("conceptos", conceptos);
        model.addAttribute("fechaInicio", LocalDate.now().withDayOfMonth(1));
        model.addAttribute("fechaFin", LocalDate.now());
        model.addAttribute("activeTab", "reporte");

        return "reporte/generador";
    }

    @GetMapping("/generar")
    public String generarReporte(
            @RequestParam(required = false) Long servicioId,
            @RequestParam(required = false) Long turnoId,
            @RequestParam(required = false) Long conceptoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            Model model) {

        List<Ausencia> resultado = null;

        if (servicioId != null && servicioId > 0) {
            resultado = reporteService.generarReporteAusenciasPorServicio(servicioId, fechaInicio, fechaFin);
            Servicio servicio = servicioService.findById(servicioId).orElse(null);
            model.addAttribute("filtro", "Servicio: " + (servicio != null ? servicio.getNombre() : ""));
        } else if (turnoId != null && turnoId > 0) {
            resultado = reporteService.generarReporteAusenciasPorTurno(turnoId, fechaInicio, fechaFin);
            Turno turno = turnoService.findById(turnoId).orElse(null);
            model.addAttribute("filtro", "Turno: " + (turno != null ? turno.getNombre() : ""));
        } else if (conceptoId != null && conceptoId > 0) {
            resultado = reporteService.generarReporteAusenciasPorConcepto(conceptoId, fechaInicio, fechaFin);
            ConceptoAusencia concepto = conceptoAusenciaService.findById(conceptoId).orElse(null);
            model.addAttribute("filtro", "Concepto: " + (concepto != null ? concepto.getNombre() : ""));
        } else {
            resultado = reporteService.generarReporteAusencias(fechaInicio, fechaFin);
            model.addAttribute("filtro", "Todos");
        }

        model.addAttribute("ausencias", resultado);
        model.addAttribute("fechaInicio", fechaInicio);
        model.addAttribute("fechaFin", fechaFin);
        model.addAttribute("totalAusencias", resultado.size());
        model.addAttribute("activeTab", "reporte");

        return "reporte/resultado";
    }

    @GetMapping("/exportar/excel")
    public ResponseEntity<byte[]> exportarExcel(
            @RequestParam(required = false) Long servicioId,
            @RequestParam(required = false) Long turnoId,
            @RequestParam(required = false) Long conceptoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<Ausencia> ausencias = null;

        if (servicioId != null && servicioId > 0) {
            ausencias = reporteService.generarReporteAusenciasPorServicio(servicioId, fechaInicio, fechaFin);
        } else if (turnoId != null && turnoId > 0) {
            ausencias = reporteService.generarReporteAusenciasPorTurno(turnoId, fechaInicio, fechaFin);
        } else if (conceptoId != null && conceptoId > 0) {
            ausencias = reporteService.generarReporteAusenciasPorConcepto(conceptoId, fechaInicio, fechaFin);
        } else {
            ausencias = reporteService.generarReporteAusencias(fechaInicio, fechaFin);
        }

        byte[] excelBytes = reporteService.exportarReporteExcel(ausencias);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_ausencias.xlsx")
                .header(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
                .body(excelBytes);
    }

    @GetMapping("/exportar/pdf")
    public ResponseEntity<byte[]> exportarPdf(
            @RequestParam(required = false) Long servicioId,
            @RequestParam(required = false) Long turnoId,
            @RequestParam(required = false) Long conceptoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {

        List<Ausencia> ausencias = null;

        if (servicioId != null && servicioId > 0) {
            ausencias = reporteService.generarReporteAusenciasPorServicio(servicioId, fechaInicio, fechaFin);
        } else if (turnoId != null && turnoId > 0) {
            ausencias = reporteService.generarReporteAusenciasPorTurno(turnoId, fechaInicio, fechaFin);
        } else if (conceptoId != null && conceptoId > 0) {
            ausencias = reporteService.generarReporteAusenciasPorConcepto(conceptoId, fechaInicio, fechaFin);
        } else {
            ausencias = reporteService.generarReporteAusencias(fechaInicio, fechaFin);
        }

        byte[] pdfBytes = reporteService.exportarReportePdf(ausencias);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_ausencias.pdf")
                .header(HttpHeaders.CONTENT_TYPE, "application/pdf")
                .body(pdfBytes);
    }
}