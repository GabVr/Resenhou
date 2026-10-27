package com.example.Resenhou.ResenhouWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ListasPageController {

    @GetMapping("/listas")
    public String listas() {
        return "listas";
    }
}
