package com.example.Resenhou.Catalogo.Model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Modelo de dados para a listagem de um gênero literário")
public class Genero {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Schema(description = "id do gênero cadastrado",  requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Column(name="nome",nullable=false)
    @NotBlank(message = "Você deve informar o nome do gênero")
    @Schema(description = "é o nome do gênero", example = "Fantasia", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Column(name="descricao",nullable=false)
    @NotBlank(message = "Você deve informar alguma descrição")
    @Schema(description = "É a descrição do gênero literário", requiredMode = Schema.RequiredMode.REQUIRED)
    private String descricao;

    @ManyToMany(mappedBy = "generos", fetch = FetchType.LAZY)
    private Set<Livro> livros = new HashSet<>();

}
