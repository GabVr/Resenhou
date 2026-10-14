package com.example.Resenhou.Catalogo.Model;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Editora {

    private Integer id;

    @NotBlank
    private String nome;

    @NotBlank
    private String pais;

    @NotBlank
    private String site;


}
