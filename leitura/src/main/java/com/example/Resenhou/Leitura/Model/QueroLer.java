package com.example.Resenhou.Leitura.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="queroler")
@Schema(description = "É o modelo de dados do QueroLer")
public class QueroLer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Schema(description = "id do QueroLer",  requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer id;

    @Column(name="data_adicao", nullable=false)
    @NotNull (message = "Deve preencher o campo de data de adição")
    @Schema(description = "É a data de adição, é quando a pessoa adiciona o livro que ela quer ler", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate dataAdicao;

    @Column(name = "usuario_id", nullable = false)
    @NotNull(message = "É importante ter o id do usuário")
    @Schema(description = "É o id do usuário", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer usuarioId;

    @Column(name = "livro_id", nullable = false)
    @NotNull(message = "É importante ter o id do livro")
    @Schema(description = "É o id do livro",  requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer livroId;
}
