package com.example.Resenhou.Leitura.Model;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "É o modelo de dados para a avaliação de livros")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Schema(description = "id da avaliação",  requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Column(name="nota", nullable=true)
    @Min(1) @Max(5)
    @Schema(description = "É a nota do livro", requiredMode = Schema.RequiredMode.REQUIRED)
    private  int nota;

    @Column(name = "usuario_id", nullable = false)
    @NotNull(message = "É importante ter o id do usuário")
    @Schema(description = "É o id do usuário", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer usuarioId;

    @Column(name="texto", nullable=true,  columnDefinition = "TEXT")
    @NotBlank(message = "Pôr o texto")
    @Schema(description = "É o texto que será escrito na avaliação", requiredMode = Schema.RequiredMode.REQUIRED)
    private  String texto;

    @Column(name="data", nullable=false)
    @NotNull(message = "É importante informar a data da avaliação")
    @Schema(description = "É a data em que a avaliação foi feita", requiredMode = Schema.RequiredMode.REQUIRED)
    private  LocalDate data;

    @Column(name = "livro_id", nullable = false)
    @NotNull(message = "É importante ter o id do livro")
    @Schema(description = "É o id do livro",  requiredMode = Schema.RequiredMode.REQUIRED)
    private  Integer livroId;

}
