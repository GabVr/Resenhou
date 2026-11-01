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
@Table(name="diario")
@Schema(description = "É o modelo de dados do Diario")
public class Diario {

   @Id
   @GeneratedValue(strategy= GenerationType.IDENTITY)
   @Schema(description = "id do diario",  requiredMode = Schema.RequiredMode.REQUIRED)
   private Integer id;

   @Column(name="data_inicio", nullable=false)
   @NotNull
   @Schema(description = "É a data de início",  requiredMode = Schema.RequiredMode.REQUIRED)
   private LocalDate dataInicio;

   @Column(name="data_termino", nullable=false)
   @NotNull
   @Schema(description = "É a data de termino do diario",  requiredMode = Schema.RequiredMode.REQUIRED)
   private  LocalDate dataTermino;

   @Column(name="paginas_lidas", nullable=false)
   @NotNull
   @Schema(description = "É a quantidade de paginas lidas no diario", requiredMode = Schema.RequiredMode.REQUIRED)
   private  int paginasLidas;

   @Column(name = "usuario_id", nullable = false)
   @NotNull(message = "É importante ter o id do usuário")
   @Schema(description = "É o id do usuário", requiredMode = Schema.RequiredMode.REQUIRED)
   private Integer usuarioId;

   @Column(name = "is_relido", nullable = false)
   @NotNull
   @Schema(description = "É para confirmar se o diario foi relido",  requiredMode = Schema.RequiredMode.REQUIRED)
   private boolean relido;

   @Column(name = "livro_id", nullable = false)
   @NotNull(message = "É importante ter o id do livro")
   @Schema(description = "É o id do livro",  requiredMode = Schema.RequiredMode.REQUIRED)
   private Integer livroId;

}
