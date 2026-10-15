package com.example.Resenhou.Leitura.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="itemlista")
public class ItemLista {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "usuario_id", nullable = false)
    @NotNull
    private Integer usuarioId;


    @Column(name = "livro_id", nullable = false)
    @NotNull
    private Integer livroId;

    @Column(name="ordem_exibicao", nullable=false)
    @PositiveOrZero
    @NotNull
    private  int ordemExibicao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lista_id", nullable = false)
    private Lista lista;


}
