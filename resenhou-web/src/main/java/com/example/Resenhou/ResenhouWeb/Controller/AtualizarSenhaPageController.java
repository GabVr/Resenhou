package com.example.Resenhou.ResenhouWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AtualizarSenhaPageController {

    @GetMapping("/atualizar-senha")
    public String atualizarSenha(){
        return "atualizar-senha";
    }

}
