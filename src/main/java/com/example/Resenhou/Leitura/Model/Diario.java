package com.example.Resenhou.Leitura.Model;

import com.example.Resenhou.Catalogo.Model.Livro;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Diario {

   private Integer id;

   @NotNull
   private LocalDate dataInicio;

   @NotNull
   private  LocalDate dataTermino;

   @NotNull
   private  int paginasLidas;

   @NotNull
   private Integer usuarioId;

   @NotNull
   private boolean relido;

   private  Livro livro;

}
