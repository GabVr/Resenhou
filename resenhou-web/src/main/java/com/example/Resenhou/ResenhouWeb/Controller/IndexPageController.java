package com.example.Resenhou.ResenhouWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexPageController {

    @GetMapping("/")
    public String inicio() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(){
        return "index";
    }

}
