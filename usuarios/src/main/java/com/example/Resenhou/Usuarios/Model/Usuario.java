package com.example.Resenhou.Usuarios.Model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="usuario")
@Schema(description = "Modelo de dados para criação de um novo usuário")
public class Usuario {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Schema(description = "id do usuário",  requiredMode = Schema.RequiredMode.REQUIRED)
    Integer id;

    @Column(name="nome", nullable=false)
    @NotBlank(message = "O campo de nome deve ser preenchido")
    @Schema(description = "Nome completo do usuário", example = "João Silva", requiredMode = Schema.RequiredMode.REQUIRED)
    String nome;

    @Column(name="username", nullable=false, unique=true)
    @NotBlank(message = "Você deve informar o seu username deve ser preenchido")
    @Schema(description = "É o username do usuário", example = "Joãozinho", requiredMode = Schema.RequiredMode.REQUIRED)
    String username;

    @Column(name="email", nullable=false, unique=true)
    @Email
    @NotBlank(message = "Você deve informar o seu email")
    @Schema(description = "E-mail do usuário", example = "João@gmail.com", requiredMode = Schema.RequiredMode.REQUIRED)
    String email;

    @Column(name="senha", nullable=false)
    @NotBlank(message = "O campo de senha deve ser preenchido")
    @Schema(description = "É a senha do usuário", example = "João@12345", requiredMode = Schema.RequiredMode.REQUIRED)
    String senha;

    @Column(name="data_cadastro", nullable=false)
    @NotNull(message = "A data de cadastro deve ser preenchida")
    @Schema(description = "Data do cadastro do usuário", example = "21-10-2026", requiredMode = Schema.RequiredMode.REQUIRED)
    LocalDate dataCadastro;

    @Column(name="biografia", nullable=true)
    @Schema(description = "É a biografia do usuário", requiredMode = Schema.RequiredMode.REQUIRED)
    String biografia;

    @Column(name = "cpf", nullable = false, unique = true)
    @NotBlank(message = "O campo de cpf deve ser preenchido")
    @Schema(description = "É o cpf do usuário", example = "652.351.020-45 ou 65235102045", requiredMode = Schema.RequiredMode.REQUIRED)
    String cpf;

}
