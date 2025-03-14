package com.nursetrack.controller;
import com.nursetrack.model.Ausencia;
import com.nursetrack.service.AusenciaService;
import com.nursetrack.service.EnfermeraService;
import com.nursetrack.service.ReporteService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final EnfermeraService enfermeraService;
    private final AusenciaService ausenciaService;
    private final ReporteService reporteService;

    public DashboardController(EnfermeraService enfermeraService,
                               AusenciaService ausenciaService,
                               ReporteService reporteService) {
        this.enfermeraService = enfermeraService;
        this.ausenciaService = ausenciaService;
        this.reporteService = reporteService;
    }



    @GetMapping({"", "/", "/index"})
    public String dashboard(Model model, Authentication authentication) {
        // Obtener estadísticas para el dashboard
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);

        // Ausencias actuales
        List<Ausencia> ausenciasHoy = ausenciaService.findByFecha(hoy);

        // Estadísticas del mes actual
        Map<String, Long> estadisticasPorConcepto = reporteService.getEstadisticasPorConcepto(inicioMes, hoy);
        Map<String, Long> estadisticasPorServicio = reporteService.getEstadisticasPorServicio(inicioMes, hoy);

        // Total de enfermeras
        long totalEnfermeras = enfermeraService.findAll().size();

        model.addAttribute("activeTab", "dashboard");
        model.addAttribute("ausenciasHoy", ausenciasHoy);
        model.addAttribute("totalAusenciasHoy", ausenciasHoy.size());

        // Handle potentially null maps to avoid Thymeleaf error
        if (estadisticasPorConcepto == null) {
            estadisticasPorConcepto = new HashMap<>();
        }
        if (estadisticasPorServicio == null) {
            estadisticasPorServicio = new HashMap<>();
        }

        model.addAttribute("estadisticasPorConcepto", estadisticasPorConcepto);
        model.addAttribute("estadisticasPorServicio", estadisticasPorServicio);
        model.addAttribute("totalEnfermeras", totalEnfermeras);

        if (authentication != null) {
            model.addAttribute("username", authentication.getName());
        }

        return "dashboard/index";
    }}