package com.example.Resenhou.Leitura.Dto;

import com.example.Resenhou.Leitura.Model.ItemLista;
import com.example.Resenhou.Leitura.Model.Lista;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record ListaRequest(
        @NotBlank String nome,
        @NotBlank String descricao,
        boolean publica,
        boolean visualicacaoPerfil,
        List<@NotNull @Positive Integer> livroIds
) {

    public Lista toEntity() {
        Lista lista = new Lista();
        lista.setNome(nome);
        lista.setDescricao(descricao);
        lista.setPublica(publica);
        lista.setVisualicacaoPerfil(visualicacaoPerfil);

        if (livroIds != null) {
            for (Integer livroId : livroIds) {
                ItemLista item = new ItemLista();
                item.setLivroId(livroId);
                lista.getItens().add(item);
            }
        }

        return lista;
    }
}