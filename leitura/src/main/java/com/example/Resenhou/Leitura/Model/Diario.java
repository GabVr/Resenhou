package com.example.Resenhou.Leitura.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="diario")
public class Diario {

   @Id
   @GeneratedValue(strategy= GenerationType.IDENTITY)
   private Integer id;

   @Column(name="data_inicio", nullable=false)
   @NotNull
   private LocalDate dataInicio;

   @Column(name="data_termino", nullable=false)
   @NotNull
   private  LocalDate dataTermino;

   @Column(name="paginas_lidas", nullable=false)
   @NotNull
   private  int paginasLidas;

   @Column(name = "usuario_id", nullable = false)
   @NotNull(message = "É importante ter o id do usuário")
   private Integer usuarioId;

   @Column(name = "is_relido", nullable = false)
   @NotNull
   private boolean relido;

   @Column(name = "livro_id", nullable = false)
   @NotNull(message = "É importante ter o id do livro")
   private Integer livroId;

}
