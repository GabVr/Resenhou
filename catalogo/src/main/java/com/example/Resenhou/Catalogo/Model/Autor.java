package com.example.Resenhou.Catalogo.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="autor")
@Schema(description = "Modelo de dados para criação de um novo autor")
public class Autor {

   @Id
   @GeneratedValue(strategy= GenerationType.IDENTITY)
   @Schema(description = "id do autor cadastrado",  requiredMode = Schema.RequiredMode.REQUIRED)
   private Integer id;

   @Column(name="nome",nullable=false)
   @NotBlank(message = "Você deve informar o nome do autor")
   @Schema(description = "nome do autor", example = "Machado de Assis", requiredMode = Schema.RequiredMode.REQUIRED)
   private String nome;

   @Column(name="data_nascimento",nullable=false)
   @NotNull(message = "Você deve informar a data de nascimento")
   @Schema(description = "é a data de nascimento do autor", example = "21-06-1839", requiredMode = Schema.RequiredMode.REQUIRED)
   private LocalDate dataNascimento;

   @Column(name="nacionalidade",nullable=false)
   @NotBlank(message = "Você deve informar a sua nacionalidade")
   @Schema(description = "nacionalidade do autor", example = "brasileiro", requiredMode = Schema.RequiredMode.REQUIRED)
   private String nacionalidade;

   @Column(name="biografia",nullable=false,  columnDefinition = "TEXT")
   @NotBlank(message = "Você deve informar a sua biografia")
   @Schema(description = "biografia do autor", requiredMode = Schema.RequiredMode.REQUIRED)
   private String biografia;

   @ManyToMany(mappedBy = "autores", fetch = FetchType.LAZY)
   private Set<Livro> livros = new HashSet<>();
}