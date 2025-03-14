package com.nursetrack.controller;

import com.nursetrack.model.Enfermera;
import com.nursetrack.model.Servicio;
import com.nursetrack.model.Turno;
import com.nursetrack.service.EnfermeraService;
import com.nursetrack.service.ServicioService;
import com.nursetrack.service.TurnoService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/enfermera")
public class EnfermeraController {

    private final EnfermeraService enfermeraService;
    private final ServicioService servicioService;
    private final TurnoService turnoService;

    public EnfermeraController(EnfermeraService enfermeraService,
                               ServicioService servicioService,
                               TurnoService turnoService) {
        this.enfermeraService = enfermeraService;
        this.servicioService = servicioService;
        this.turnoService = turnoService;
    }

    @GetMapping({"", "/", "/lista"})
    public String listarEnfermeras(Model model) {
        List<Enfermera> enfermeras = enfermeraService.findAll();
        model.addAttribute("activeTab", "enfermera");
        model.addAttribute("enfermeras", enfermeras);
        return "enfermera/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        Enfermera enfermera = new Enfermera();
        List<Servicio> servicios = servicioService.findAllActivos();
        List<Turno> turnos = turnoService.findAllActivos();

        model.addAttribute("enfermera", enfermera);
        model.addAttribute("servicios", servicios);
        model.addAttribute("turnos", turnos);
        model.addAttribute("titulo", "Registrar Nueva Enfermera");

        return "enfermera/form";
    }

    @PostMapping("/guardar")
    public String guardarEnfermera(@Valid Enfermera enfermera, BindingResult result,
                                   Model model, RedirectAttributes flash,
                                   Authentication authentication) {
        if (result.hasErrors()) {
            List<Servicio> servicios = servicioService.findAllActivos();
            List<Turno> turnos = turnoService.findAllActivos();

            model.addAttribute("servicios", servicios);
            model.addAttribute("turnos", turnos);
            model.addAttribute("titulo", enfermera.getId() != null ? "Editar Enfermera" : "Registrar Nueva Enfermera");

            return "enfermera/form";
        }

        if (enfermera.getId() == null) {
            enfermera.setFechaCreacion(LocalDate.now());
            enfermera.setUsuarioCreacion(authentication.getName());
        }

        enfermeraService.save(enfermera);
        flash.addFlashAttribute("success", "Enfermera guardada con éxito");
        return "redirect:/enfermera";
    }

    @GetMapping("/editar/{id}")
    public String editarEnfermera(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Enfermera enfermera = null;

        if (id > 0) {
            enfermera = enfermeraService.findById(id).orElse(null);
            if (enfermera == null) {
                flash.addFlashAttribute("error", "La enfermera no existe");
                return "redirect:/enfermera";
            }
        } else {
            flash.addFlashAttribute("error", "ID de enfermera inválido");
            return "redirect:/enfermera";
        }

        List<Servicio> servicios = servicioService.findAllActivos();
        List<Turno> turnos = turnoService.findAllActivos();

        model.addAttribute("enfermera", enfermera);
        model.addAttribute("servicios", servicios);
        model.addAttribute("turnos", turnos);
        model.addAttribute("titulo", "Editar Enfermera");

        return "enfermera/form";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Enfermera enfermera = enfermeraService.findById(id).orElse(null);
        if (enfermera == null) {
            flash.addFlashAttribute("error", "La enfermera no existe");
            return "redirect:/enfermera";
        }

        model.addAttribute("enfermera", enfermera);
        model.addAttribute("titulo", "Detalle de Enfermera");

        return "enfermera/detalle";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarEnfermera(@PathVariable Long id, RedirectAttributes flash) {
        if (id > 0) {
            enfermeraService.deleteById(id);
            flash.addFlashAttribute("success", "Enfermera eliminada con éxito");
        }
        return "redirect:/enfermera";
    }

    @GetMapping("/buscar")
    public String buscarEnfermeras(@RequestParam String termino, Model model) {
        List<Enfermera> enfermeras = enfermeraService.search(termino);
        model.addAttribute("enfermeras", enfermeras);
        model.addAttribute("titulo", "Resultados de la búsqueda: " + termino);
        return "enfermera/lista";
    }
}

