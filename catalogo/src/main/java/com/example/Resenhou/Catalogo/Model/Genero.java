package com.example.Resenhou.Catalogo.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name="genero")
public class Genero {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="nome",nullable=false)
    @NotBlank(message = "Você deve informar o nome do gênero")
    private String nome;

    @Column(name="descricao",nullable=false)
    @NotBlank(message = "Você deve informar alguma descrição")
    private String descricao;

    @ManyToMany(mappedBy = "generos", fetch = FetchType.LAZY)
    private Set<Livro> livros = new HashSet<>();

}
