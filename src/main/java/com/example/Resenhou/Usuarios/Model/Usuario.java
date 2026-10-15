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
    @NotBlank
    String nome;

    @Column(name="username", nullable=false, unique=true)
    @NotBlank
    String username;

    @Column(name="email", nullable=false, unique=true)
    @Email
    @NotBlank
    String email;

    @Column(name="senha", nullable=false, unique=true)
    @NotBlank
    String senha;

    @Column(name="data_cadastro", nullable=false)
    @NotNull
    LocalDate dataCadastro;

    @Column(name="biografia", nullable=false)
    @NotBlank
    String biografia;

}
