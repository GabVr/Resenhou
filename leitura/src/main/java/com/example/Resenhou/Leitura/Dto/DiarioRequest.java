package com.example.Resenhou.Leitura.Dto;

import com.example.Resenhou.Leitura.Model.Diario;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record DiarioRequest(
        @NotNull @Positive Integer livroId,
        @NotNull LocalDate dataInicio,
        LocalDate dataTermino,
        @NotNull @PositiveOrZero Integer paginasLidas,
        boolean relido
) {

    public Diario toEntity() {
        Diario diario = new Diario();
        diario.setLivroId(livroId);
        diario.setDataInicio(dataInicio);
        diario.setDataTermino(dataTermino);
        diario.setPaginasLidas(paginasLidas);
        diario.setRelido(relido);

        return diario;
    }
}