package com.example.Resenhou.Leitura.Model;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="itemlista")
@Schema(description = "É o modelo de dados do ItemLista")
public class ItemLista {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Schema(description = "id do item da lista",  requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Column(name = "usuario_id", nullable = false)
    @NotNull(message = "É importante ter o id do usuário")
    @Schema(description = "É o id do usuário", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer usuarioId;


    @Column(name = "livro_id", nullable = false)
    @NotNull(message = "É importante ter o id do livro")
    @Schema(description = "É o id do livro",  requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer livroId;

    @Column(name="ordem_exibicao", nullable=false)
    @PositiveOrZero
    @NotNull
    @Schema(description = "É a ordem de exibição do item que está na lista", requiredMode = Schema.RequiredMode.REQUIRED)
    private  int ordemExibicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lista_id", nullable = false)
    private Lista lista;


}
