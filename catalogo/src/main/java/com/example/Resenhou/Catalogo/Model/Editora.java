package com.example.Resenhou.Catalogo.Model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Modelo de dados para criação de uma nova editora")
public class Editora {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Schema(description = "id da editora cadastrado",  requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Column(name="nome",nullable=false)
    @NotBlank(message = "Você deve informar o nome da editora")
    @Schema(description = "É o nome da editora", requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Column(name="pais",nullable=false)
    @NotBlank(message = "Você deve informar o nome do país onde está localizado")
    @Schema(description ="É o país da editora",example = "Brasil",  requiredMode=Schema.RequiredMode.REQUIRED)
    private String pais;

    @Column(name="site",nullable=false)
    @NotBlank(message = "Você deve informar o nome do site")
    @Schema(description = "É para ser o site da editora (principalmente o link da mesma)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String site;

    @OneToMany(mappedBy = "editora", fetch = FetchType.LAZY)
    private List<Livro> livros = new ArrayList<>();
}
