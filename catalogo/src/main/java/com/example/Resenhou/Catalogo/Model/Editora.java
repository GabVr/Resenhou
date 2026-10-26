package com.example.Resenhou.Catalogo.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="editora")
public class Editora {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="nome",nullable=false)
    @NotBlank(message = "Você deve informar o nome da editora")
    private String nome;

    @Column(name="pais",nullable=false)
    @NotBlank(message = "Você deve informar o nome do país onde está localizado")
    private String pais;

    @Column(name="site",nullable=false)
    @NotBlank(message = "Você deve informar o nome do site")
    private String site;

    @OneToMany(mappedBy = "editora", fetch = FetchType.LAZY)
    private List<Livro> livros = new ArrayList<>();
}
