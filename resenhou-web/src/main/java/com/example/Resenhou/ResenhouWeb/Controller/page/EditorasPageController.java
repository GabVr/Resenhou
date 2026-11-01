package com.example.Resenhou.ResenhouWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EditorasPageController {

    @GetMapping("/editoras")
    public String catalogo(){
        return "editoras";
    }


}
