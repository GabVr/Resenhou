package com.example.Resenhou.Leitura.Model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Lista {

    private Integer id;

    @NotBlank
    private String nome;

    @NotBlank
    private String descricao;

    @NotNull
    private LocalDate dataCriacao;

    @NotNull
    private Integer usuarioId;

    @NotNull
    private boolean visualicacaoPerfil;

    private List<ItemLista> itens = new ArrayList<>();


}
