package com.example.Resenhou.Leitura.Model;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Avaliacao {

    private Integer id;

    @NotNull
    private  int nota;

    @NotNull
    private Integer usuarioId;

    @NotBlank
    private  String texto;

    @NotNull
    private  LocalDate data;

    @NotNull
    private  Integer livroId;

}
