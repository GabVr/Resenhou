package com.example.Resenhou.Catalogo.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Livro {

    private  Integer id;

    @NotBlank
    private String nome;

    @NotBlank
    private String titulo;

    @NotBlank
    private String sinopse;

    @NotNull
    private LocalDate dataPublicacao;

    @NotNull
    private int numeroPaginas;

    @NotBlank
    private String isbn;

    @NotBlank
    private String tituloOriginal;

    private GeneroLiterario generoLiterario;

    private Autor autor;
    private Editora editora;
}
