package com.example.Resenhou.ResenhouWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import java.security.Principal;

@Controller
public class PerfilPageController {

    @GetMapping("/perfil")
    public String perfil(Principal principal, Model model) {
        model.addAttribute("nomeUsuario", principal.getName());
        return "perfil";
    }
}
