package com.example.Resenhou.Leitura.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="queroler")
public class QueroLer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="data_adicao", nullable=false)
    @NotNull
    private LocalDate dataAdicao;

    @Column(name = "usuario_id", nullable = false)
    @NotNull
    private Integer usuarioId;

    @Column(name = "livro_id", nullable = false)
    @NotNull
    private Integer livroId;
}
