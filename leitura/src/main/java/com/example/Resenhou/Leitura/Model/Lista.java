package com.example.Resenhou.Leitura.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="lista")
public class Lista {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="nome", nullable=false)
    @NotBlank(message ="É importante ter o nome da lista")
    private String nome;

    @Column(name="descricao", nullable=true, columnDefinition = "TEXT")
    @NotBlank(message = "É importante ter a descrição da lista")
    private String descricao;

    @Column(name="data_criacao", nullable=false)
    @NotNull
    private LocalDate dataCriacao;

    @Column(name = "is_publica", nullable = false)
    private boolean publica;

    @Column(name = "usuario_id", nullable = false)
    @NotNull(message = "É importante ter o id do usuário")
    private Integer usuarioId;

    @Column(name = "livro_id", nullable = false)
    @NotNull(message = "É importante ter o id do livro")
    private Integer livroId;

    @Column(name="visualizacao_perfil", nullable=false)
    @NotNull
    private boolean visualicacaoPerfil;

    @OneToMany(mappedBy = "lista", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("ordemExibicao ASC")
    private List<ItemLista> itens = new ArrayList<>();
}