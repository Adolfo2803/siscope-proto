package com.nursetrack.controller;

import com.nursetrack.model.Servicio;
import com.nursetrack.service.ServicioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/servicio")
public class ServicioController {

    private final ServicioService servicioService;

    public ServicioController(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @GetMapping
    public String listar(Model model) {
        List<Servicio> servicios = servicioService.findAll();
        model.addAttribute("servicios", servicios);
        return "servicio/lista";
    }

    @GetMapping("/nuevo")
    public String formularioNuevo(Model model) {
        model.addAttribute("servicio", new Servicio());
        model.addAttribute("titulo", "Crear Servicio");
        return "servicio/form";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditar(@PathVariable("id") Long id, Model model, RedirectAttributes flash) {
        if (id <= 0) {
            flash.addFlashAttribute("error", "El ID del servicio no puede ser cero o negativo");
            return "redirect:/servicio";
        }

        Servicio servicio = servicioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de servicio inválido: " + id));

        model.addAttribute("servicio", servicio);
        model.addAttribute("titulo", "Editar Servicio");
        return "servicio/form";
    }

    @PostMapping("/guardar")
    public String guardar(@Valid Servicio servicio, BindingResult result, Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            model.addAttribute("titulo", servicio.getId() != null ? "Editar Servicio" : "Crear Servicio");
            return "servicio/form";
        }

        String mensajeFlash = (servicio.getId() != null) ? "Servicio editado con éxito" : "Servicio creado con éxito";

        servicioService.save(servicio);
        flash.addFlashAttribute("success", mensajeFlash);
        return "redirect:/servicio";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable("id") Long id, RedirectAttributes flash) {
        if (id <= 0) {
            flash.addFlashAttribute("error", "El ID del servicio no puede ser cero o negativo");
            return "redirect:/servicio";
        }

        servicioService.deleteById(id);
        flash.addFlashAttribute("success", "Servicio eliminado con éxito");
        return "redirect:/servicio";
    }

    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable("id") Long id, Model model, RedirectAttributes flash) {
        if (id <= 0) {
            flash.addFlashAttribute("error", "El ID del servicio no puede ser cero o negativo");
            return "redirect:/servicio";
        }

        Servicio servicio = servicioService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID de servicio inválido: " + id));

        model.addAttribute("servicio", servicio);
        return "servicio/detalle";
    }
}