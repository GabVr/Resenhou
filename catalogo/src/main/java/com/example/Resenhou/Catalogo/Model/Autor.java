package com.example.Resenhou.Catalogo.Model;

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
public class Autor {

   @Id
   @GeneratedValue(strategy= GenerationType.IDENTITY)
   private Integer id;

   @Column(name="nome",nullable=false)
   @NotBlank(message = "Você deve informar o nome do autor")
   private String nome;

   @Column(name="data_nascimento",nullable=false)
   @NotNull(message = "Você deve informar a data de nascimento")
   private LocalDate dataNascimento;

   @Column(name="nacionalidade",nullable=false)
   @NotBlank(message = "Você deve informar a sua nacionalidade")
   private String nacionalidade;

   @Column(name="biografia",nullable=false,  columnDefinition = "TEXT")
   @NotBlank(message = "Você deve informar a sua biografia")
   private String biografia;

   @ManyToMany(mappedBy = "autores", fetch = FetchType.LAZY)
   private Set<Livro> livros = new HashSet<>();
}