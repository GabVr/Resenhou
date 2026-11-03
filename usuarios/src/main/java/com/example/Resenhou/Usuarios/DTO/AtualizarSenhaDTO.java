
package com.example.Resenhou.Usuarios.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AtualizarSenhaDTO {

    @NotBlank(message = "O e-mail deve ser informado.")
    @Email(message = "Informe um e-mail válido.")
    private String email;

    @NotBlank(message = "A senha atual deve ser informada.")
    private String senhaAtual;

    @NotBlank(message = "A nova senha deve ser informada.")
    @Size(min = 8, message = "A nova senha deve possuir pelo menos 8 caracteres.")
    private String novaSenha;
}
