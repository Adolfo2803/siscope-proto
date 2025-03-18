package com.nursetrack.controller;

import com.nursetrack.model.Turno;
import com.nursetrack.service.TurnoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/turno")
public class TurnoController {

    private final TurnoService turnoService;

    public TurnoController(TurnoService turnoService) {
        this.turnoService = turnoService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Turno> turnos = turnoService.findAll();
        model.addAttribute("turnos", turnos);
        return "turno/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("turno", new Turno());
        model.addAttribute("titulo", "Crear Turno");
        return "turno/form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable("id") Long id, Model model, RedirectAttributes flash) {
        if (id <= 0) {
            flash.addFlashAttribute("error", "El ID del turno no puede ser cero o negativo");
            return "redirect:/turno";
        }

        Turno turno = turnoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de turno inválido: " + id));

        model.addAttribute("turno", turno);
        model.addAttribute("titulo", "Editar Turno");
        return "turno/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Turno turno, BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", turno.getId() != null ? "Editar Turno" : "Crear Turno");
            return "turno/form";
        }

        String mensajeFlash = (turno.getId() != null) ? "Turno editado con éxito" : "Turno creado con éxito";

        turnoService.save(turno);
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/turno";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes flash) {
        if (id <= 0) {
            flash.addFlashAttribute("error", "El ID del turno no puede ser cero o negativo");
            return "redirect:/turno";
        }

        turnoService.deleteById(id);
        flash.addFlashAttribute("success", "Turno eliminado con éxito");
        return "redirect:/turno";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable("id") Long id, Model model, RedirectAttributes flash) {
        if (id <= 0) {
            flash.addFlashAttribute("error", "El ID del turno no puede ser cero o negativo");
            return "redirect:/turno";
        }

        Turno turno = turnoService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de turno inválido: " + id));

        model.addAttribute("turno", turno);
        return "turno/detalle";
    }
}