package com.example.Resenhou.ResenhouWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CadastroPageController {

    @GetMapping("/cadastro-senha")
    public String cadastro(){
        return "cadastro-senha";
    }
}
