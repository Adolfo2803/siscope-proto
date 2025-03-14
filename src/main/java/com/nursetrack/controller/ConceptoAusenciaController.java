package com.nursetrack.controller;


import com.nursetrack.model.ConceptoAusencia;
import com.nursetrack.service.ConceptoAusenciaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/concepto")
public class ConceptoAusenciaController {

    private final ConceptoAusenciaService conceptoAusenciaService;

    public ConceptoAusenciaController(ConceptoAusenciaService conceptoAusenciaService) {
        this.conceptoAusenciaService = conceptoAusenciaService;
    }

    @GetMapping
    public String listarConceptos(Model model) {
        List<ConceptoAusencia> conceptos = conceptoAusenciaService.findAll();
        model.addAttribute("conceptos", conceptos);
        model.addAttribute("activeTab", "concepto");
        return "concepto/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        ConceptoAusencia concepto = new ConceptoAusencia();

        model.addAttribute("concepto", concepto);
        model.addAttribute("titulo", "Crear Nuevo Concepto de Ausencia");

        return "concepto/form";
    }

    @PostMapping("/guardar")
    public String guardarConcepto(@Valid ConceptoAusencia concepto, BindingResult result,
                                  Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", concepto.getId() != null ? "Editar Concepto de Ausencia" : "Crear Nuevo Concepto de Ausencia");
            return "concepto/form";
        }

        conceptoAusenciaService.save(concepto);
        flash.addFlashAttribute("success", "Concepto guardado con éxito");
        return "redirect:/concepto";
    }

    @GetMapping("/editar/{id}")
    public String editarConcepto(@PathVariable Long id, Model model, RedirectAttributes flash) {
        ConceptoAusencia concepto = null;

        if (id > 0) {
            concepto = conceptoAusenciaService.findById(id).orElse(null);
            if (concepto == null) {
                flash.addFlashAttribute("error", "El concepto no existe");
                return "redirect:/concepto";
            }
        } else {
            flash.addFlashAttribute("error", "ID de concepto inválido");
            return "redirect:/concepto";
        }

        model.addAttribute("concepto", concepto);
        model.addAttribute("titulo", "Editar Concepto de Ausencia");

        return "concepto/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarConcepto(@PathVariable Long id, RedirectAttributes flash) {
        if (id > 0) {
            conceptoAusenciaService.deleteById(id);
            flash.addFlashAttribute("success", "Concepto eliminado con éxito");
        }
        return "redirect:/concepto";
    }
}
