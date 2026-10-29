package com.example.Resenhou.ResenhouWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AutoresPageController {

    @GetMapping("/autores")
    public String catalogo(){
        return "autores";
    }
}