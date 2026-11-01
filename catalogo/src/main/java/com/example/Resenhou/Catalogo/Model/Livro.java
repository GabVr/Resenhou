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
@Table(name="livro")
@Schema(description = "Modelo de dados para criação de um novo livro")
public class Livro {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Schema(description = "id do livro cadastrado",  requiredMode = Schema.RequiredMode.REQUIRED)
    private  Integer id;

    @Column(name="nome",nullable=false)
    @NotBlank(message = "Você deve informar o nome do livro")
    @Schema(description = "nome do livro", example = "Dom Casmurro" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Column(name="titulo",nullable=false)
    @NotBlank(message = "Você deve informar título do livro")
    @Schema(description = "título do livro", example = "Dom Casmurro" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private String titulo;

    @Column(name="sinopse",nullable=false,  columnDefinition = "TEXT")
    @NotBlank(message = "Você deve pôr uma sinopse")
    @Schema(description = "É a sinopse do livro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sinopse;

    @Column(name="data_nascimento",nullable=false)
    @NotNull(message = "A data de publicação não pode ser vazia")
    @Schema(description = "É a data de publicação do livro",example = "31-10-2026" ,requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dataPublicacao;

    @Column(name="numero_paginas",nullable=false)
    @NotNull (message = "Você deve informar o número de páginas")
    @Schema(description = "É o número de páginas", requiredMode = Schema.RequiredMode.REQUIRED)
    private int numeroPaginas;

    @Column(name="isbn",nullable=false, unique=true)
    @NotBlank(message = "Você deve preencher o campo de isbn")
    @Schema(description = "É o isbn do livro", example = "978-8594318602", requiredMode = Schema.RequiredMode.REQUIRED)
    private String isbn;

    @Column(name="titulo_original",nullable=false)
    @NotBlank(message = "Você deve preencher o campo com o título original do livro")
    @Schema(description = "É o título original do livro", requiredMode = Schema.RequiredMode.REQUIRED)
    private String tituloOriginal;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_livro_genero",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private Set<Genero> generos = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tb_livro_autores",
            joinColumns = @JoinColumn(name = "livro_id"),
            inverseJoinColumns = @JoinColumn(name = "autor_id")
    )
    private Set<Autor> autores = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editora_id", nullable = false)
    private Editora editora;
}
