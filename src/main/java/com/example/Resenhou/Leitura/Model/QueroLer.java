package com.example.Resenhou.Leitura.Model;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class QueroLer {

    private Integer id;

    @NotNull
    private LocalDate dataAdicao;

    @NotNull
    private Integer usuarioId;

    @NotNull
    private Integer livroId;
}
