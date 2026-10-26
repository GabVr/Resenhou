package com.example.Resenhou.Usuarios.Model;

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
public class Usuario {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Integer id;

    @Column(name="nome", nullable=false)
    @NotBlank(message = "O campo de nome deve ser preenchido")
    String nome;

    @Column(name="username", nullable=false, unique=true)
    @NotBlank(message = "Você deve informar o seu username deve ser preenchido")
    String username;

    @Column(name="email", nullable=false, unique=true)
    @Email
    @NotBlank(message = "Você deve informar o seu email")
    String email;

    @Column(name="senha", nullable=false)
    @NotBlank(message = "O campo de senha deve ser preenchido")
    String senha;

    @Column(name="data_cadastro", nullable=false)
    @NotNull(message = "A data de cadastro deve ser preenchida")
    LocalDate dataCadastro;

    @Column(name="biografia", nullable=false)
    @NotBlank(message = "O campo de biografia deve ser preenchido")
    String biografia;

    @Column(name = "cpf", nullable = false, unique = true)
    @NotBlank(message = "O campo de cpf deve ser preenchido")
    String cpf;

}
