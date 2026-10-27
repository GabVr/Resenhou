package com.example.Resenhou.ResenhouWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class QueroTerPageController {

    @GetMapping("/queroter")
    public String queroTer() {
        return "quero-ter";
    }

}
