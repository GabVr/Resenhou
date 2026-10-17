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
@Table(name="livro")
public class Livro {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private  Integer id;

    @Column(name="nome",nullable=false)
    @NotBlank
    private String nome;

    @Column(name="titulo",nullable=false)
    @NotBlank
    private String titulo;

    @Column(name="sinopse",nullable=false,  columnDefinition = "TEXT")
    @NotBlank
    private String sinopse;

    @Column(name="data_nascimento",nullable=false)
    @NotNull
    private LocalDate dataPublicacao;

    @Column(name="numero_paginas",nullable=false)
    @NotNull
    private int numeroPaginas;

    @Column(name="isbn",nullable=false, unique=true)
    @NotBlank
    private String isbn;

    @Column(name="titulo_original",nullable=false)
    @NotBlank
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
