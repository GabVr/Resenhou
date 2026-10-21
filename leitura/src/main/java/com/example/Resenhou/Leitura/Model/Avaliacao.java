package com.example.Resenhou.Leitura.Model;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="avaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="nota", nullable=false)
    @NotNull
    @Min(1) @Max(5)
    private  int nota;

    @Column(name = "usuario_id", nullable = false)
    @NotNull
    private Integer usuarioId;

    @Column(name="texto", nullable=true,  columnDefinition = "TEXT")
    @NotBlank
    private  String texto;

    @Column(name="data", nullable=false)
    @NotNull
    private  LocalDate data;

    @Column(name = "livro_id", nullable = false)
    @NotNull
    private  Integer livroId;

}
