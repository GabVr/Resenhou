package com.example.Resenhou.Usuarios.Model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Usuario {

    Integer id;

    @NotBlank
    String nome;

    @NotBlank
    String username;

    @Email
    @NotBlank
    String email;

    @NotBlank
    String senha;

    @NotNull
    LocalDate dataCadastro;

    @NotBlank
    String biografia;

}
