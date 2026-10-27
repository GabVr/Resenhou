package com.example.Resenhou.ResenhouWeb.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CatalogoPageController {

  @GetMapping("/catalogo")
  public String catalogo(){
      return "catalogo";
  }


}
