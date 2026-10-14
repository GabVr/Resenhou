package com.example.Resenhou.Leitura.Model;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemLista {

    private Integer id;

    @NotNull
    private Integer usuarioId;

    @NotNull
    private Integer livroId;

    @PositiveOrZero
    @NotNull
    private  int ordemExibicao;

    private Lista lista;


}
