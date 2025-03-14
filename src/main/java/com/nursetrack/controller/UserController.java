package com.nursetrack.controller;
import com.nursetrack.model.Role;
import com.nursetrack.model.User;
import com.nursetrack.service.RoleService;
import com.nursetrack.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;

    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String listarUsuarios(Model model) {
        List<User> usuarios = userService.findAll();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("activeTab", "usuario");
        return "usuario/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        User user = new User();
        List<Role> roles = roleService.findAll();

        model.addAttribute("usuario", user);
        model.addAttribute("roles", roles);
        model.addAttribute("titulo", "Crear Nuevo Usuario");

        return "usuario/form";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@Valid User user, BindingResult result,
                                 Model model, RedirectAttributes flash) {
        if (result.hasErrors()) {
            List<Role> roles = roleService.findAll();

            model.addAttribute("roles", roles);
            model.addAttribute("titulo", user.getId() != null ? "Editar Usuario" : "Crear Nuevo Usuario");

            return "usuario/form";
        }

        // Validar username único
        if (user.getId() == null && userService.existsByUsername(user.getUsername())) {
            flash.addFlashAttribute("error", "El nombre de usuario ya existe");
            return "redirect:/usuario/nuevo";
        }

        // Validar email único
        if (user.getId() == null && user.getEmail() != null && !user.getEmail().isEmpty()
                && userService.existsByEmail(user.getEmail())) {
            flash.addFlashAttribute("error", "El email ya está registrado");
            return "redirect:/usuario/nuevo";
        }

        userService.save(user);
        flash.addFlashAttribute("success", "Usuario guardado con éxito");
        return "redirect:/usuario";
    }

    @GetMapping("/editar/{id}")
    public String editarUsuario(@PathVariable Long id, Model model, RedirectAttributes flash) {
        User user = null;

        if (id > 0) {
            user = userService.findById(id).orElse(null);
            if (user == null) {
                flash.addFlashAttribute("error", "El usuario no existe");
                return "redirect:/usuario";
            }
        } else {
            flash.addFlashAttribute("error", "ID de usuario inválido");
            return "redirect:/usuario";
        }

        List<Role> roles = roleService.findAll();

        model.addAttribute("usuario", user);
        model.addAttribute("roles", roles);
        model.addAttribute("titulo", "Editar Usuario");

        return "usuario/form";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Long id, RedirectAttributes flash) {
        if (id > 0) {
            userService.deleteById(id);
            flash.addFlashAttribute("success", "Usuario eliminado con éxito");
        }
        return "redirect:/usuario";
    }
}