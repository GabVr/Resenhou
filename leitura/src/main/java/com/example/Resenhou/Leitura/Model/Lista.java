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
    @NotBlank
    private String nome;

    @Column(name="descricao", nullable=true, columnDefinition = "TEXT")
    @NotBlank
    private String descricao;

    @Column(name="data_criacao", nullable=false)
    @NotNull
    private LocalDate dataCriacao;

    @Column(name = "is_publica", nullable = false)
    private boolean publica;

    @Column(name = "usuario_id", nullable = false)
    @NotNull
    private Integer usuarioId;

    @Column(name="visualizacao_perfil", nullable=false)
    @NotNull
    private boolean visualicacaoPerfil;

    @OneToMany(mappedBy = "lista", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("ordemExibicao ASC")
    private List<ItemLista> itens = new ArrayList<>();
}