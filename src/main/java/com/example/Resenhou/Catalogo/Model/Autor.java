package com.example.Resenhou.Catalogo.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Autor {

   private Integer id;

   @NotBlank
   private String nome;

   @NotNull
   private LocalDate dataNascimento;

   @NotBlank
   private String nacionalidade;

   @NotBlank
   private String biografia;
   
   Livro livro;

}
