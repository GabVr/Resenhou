package com.example.Resenhou.Leitura.Model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "É o modelo de dados da Lista")
public class Lista {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Schema(description = "id da lista",  requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Column(name="nome", nullable=false)
    @NotBlank(message ="É importante ter o nome da lista")
    @Schema(description = "É o nome da lista",  requiredMode = Schema.RequiredMode.REQUIRED)
    private String nome;

    @Column(name="descricao", nullable=true, columnDefinition = "TEXT")
    @NotBlank(message = "É importante ter a descrição da lista")
    @Schema(description = "É a descrição da lista",  requiredMode = Schema.RequiredMode.REQUIRED)
    private String descricao;

    @Column(name="data_criacao", nullable=false)
    @NotNull(message = "Deve ter uma data de criação")
    @Schema(description = "É a data de criação da lista", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dataCriacao;

    @Column(name = "is_publica", nullable = false)
    @Schema(description = "Determina se a lista é pública ou não", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean publica;

    @Column(name = "usuario_id", nullable = false)
    @NotNull(message = "É importante ter o id do usuário")
    @Schema(description = "É o id do usuário", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer usuarioId;

    @Column(name = "livro_id", nullable = false)
    @NotNull(message = "É importante ter o id do livro")
    @Schema(description = "É o id do livro",  requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer livroId;

    @Column(name="visualizacao_perfil", nullable=true)
    @Schema(description = "É a visualização do perfil", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean visualicacaoPerfil;

    @OneToMany(mappedBy = "lista", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("ordemExibicao ASC")
    private List<ItemLista> itens = new ArrayList<>();
}