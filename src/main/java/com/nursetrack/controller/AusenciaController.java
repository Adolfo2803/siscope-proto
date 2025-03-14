package com.nursetrack.controller;

import java.time.LocalDate;
import com.nursetrack.model.Ausencia;
import com.nursetrack.model.ConceptoAusencia;
import com.nursetrack.model.Enfermera;
import com.nursetrack.service.AusenciaService;
import com.nursetrack.service.ConceptoAusenciaService;
import com.nursetrack.service.EnfermeraService;
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
@RequestMapping("/ausencia")
public class AusenciaController {

    private final AusenciaService ausenciaService;
    private final EnfermeraService enfermeraService;
    private final ConceptoAusenciaService conceptoAusenciaService;

    public AusenciaController(AusenciaService ausenciaService,
                              EnfermeraService enfermeraService,
                              ConceptoAusenciaService conceptoAusenciaService) {
        this.ausenciaService = ausenciaService;
        this.enfermeraService = enfermeraService;
        this.conceptoAusenciaService = conceptoAusenciaService;
    }



    @GetMapping
    public String listarAusencias(Model model) {
        List<Ausencia> ausencias = ausenciaService.findAll();
        model.addAttribute("activeTab", "ausencia");
        model.addAttribute("ausencias", ausencias);
        return "ausencia/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        Ausencia ausencia = new Ausencia();
        ausencia.setFechaInicio(LocalDate.now());
        ausencia.setFechaFin(LocalDate.now());

        List<Enfermera> enfermeras = enfermeraService.findAll();
        List<ConceptoAusencia> conceptos = conceptoAusenciaService.findAllActivos();

        model.addAttribute("ausencia", ausencia);
        model.addAttribute("enfermeras", enfermeras);
        model.addAttribute("conceptos", conceptos);
        model.addAttribute("titulo", "Registrar Nueva Ausencia");

        return "ausencia/form";
    }

    @GetMapping("/nuevo/{enfermeraId}")
    public String mostrarFormularioNuevoParaEnfermera(@PathVariable Long enfermeraId, Model model,
                                                      RedirectAttributes flash) {
        Enfermera enfermera = enfermeraService.findById(enfermeraId).orElse(null);
        if (enfermera == null) {
            flash.addFlashAttribute("error", "La enfermera no existe");
            return "redirect:/enfermera";
        }

        Ausencia ausencia = new Ausencia();
        ausencia.setEnfermera(enfermera);
        ausencia.setFechaInicio(LocalDate.now());
        ausencia.setFechaFin(LocalDate.now());

        List<ConceptoAusencia> conceptos = conceptoAusenciaService.findAllActivos();

        model.addAttribute("ausencia", ausencia);
        model.addAttribute("enfermeras", List.of(enfermera));
        model.addAttribute("conceptos", conceptos);
        model.addAttribute("titulo", "Registrar Ausencia para " + enfermera.getNombre() + " "
                + enfermera.getApellidoPaterno() + " " + enfermera.getApellidoMaterno());

        return "ausencia/form";
    }

    @PostMapping("/guardar")
    public String guardarAusencia(@Valid Ausencia ausencia, BindingResult result,
                                  Model model, RedirectAttributes flash,
                                  Authentication authentication) {
        if (result.hasErrors()) {
            List<Enfermera> enfermeras = enfermeraService.findAll();
            List<ConceptoAusencia> conceptos = conceptoAusenciaService.findAllActivos();

            model.addAttribute("enfermeras", enfermeras);
            model.addAttribute("conceptos", conceptos);
            model.addAttribute("titulo", ausencia.getId() != null ? "Editar Ausencia" : "Registrar Nueva Ausencia");

            return "ausencia/form";
        }

        if (ausencia.getFechaInicio().isAfter(ausencia.getFechaFin())) {
            flash.addFlashAttribute("error", "La fecha de inicio no puede ser posterior a la fecha de fin");
            return "redirect:/ausencia/nuevo";
        }

        ausencia.setFechaRegistro(LocalDate.now());
        ausencia.setUsuarioRegistro(authentication.getName());

        ausenciaService.save(ausencia);
        flash.addFlashAttribute("success", "Ausencia guardada con éxito");
        return "redirect:/ausencia";
    }

    @GetMapping("/editar/{id}")
    public String editarAusencia(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Ausencia ausencia = null;

        if (id > 0) {
            ausencia = ausenciaService.findById(id).orElse(null);
            if (ausencia == null) {
                flash.addFlashAttribute("error", "La ausencia no existe");
                return "redirect:/ausencia";
            }
        } else {
            flash.addFlashAttribute("error", "ID de ausencia inválido");
            return "redirect:/ausencia";
        }

        List<Enfermera> enfermeras = enfermeraService.findAll();
        List<ConceptoAusencia> conceptos = conceptoAusenciaService.findAllActivos();

        model.addAttribute("ausencia", ausencia);
        model.addAttribute("enfermeras", enfermeras);
        model.addAttribute("conceptos", conceptos);
        model.addAttribute("titulo", "Editar Ausencia");

        return "ausencia/form";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalle(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Ausencia ausencia = ausenciaService.findById(id).orElse(null);
        if (ausencia == null) {
            flash.addFlashAttribute("error", "La ausencia no existe");
            return "redirect:/ausencia";
        }

        model.addAttribute("ausencia", ausencia);
        model.addAttribute("titulo", "Detalle de Ausencia");

        return "ausencia/detalle";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarAusencia(@PathVariable Long id, RedirectAttributes flash) {
        if (id > 0) {
            ausenciaService.deleteById(id);
            flash.addFlashAttribute("success", "Ausencia eliminada con éxito");
        }
        return "redirect:/ausencia";
    }

    @GetMapping("/enfermera/{id}")
    public String listarAusenciasPorEnfermera(@PathVariable Long id, Model model, RedirectAttributes flash) {
        Enfermera enfermera = enfermeraService.findById(id).orElse(null);
        if (enfermera == null) {
            flash.addFlashAttribute("error", "La enfermera no existe");
            return "redirect:/enfermera";
        }

        List<Ausencia> ausencias = ausenciaService.findByEnfermeraId(id);

        model.addAttribute("ausencias", ausencias);
        model.addAttribute("enfermera", enfermera);
        model.addAttribute("titulo", "Ausencias de " + enfermera.getNombre() + " "
                + enfermera.getApellidoPaterno() + " " + enfermera.getApellidoMaterno());

        return "ausencia/lista";
    }
}
