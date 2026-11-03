package com.example.Resenhou.Leitura.Config;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UsuarioAtual {

    public Integer getId() {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para continuar"
            );
        }

        Object valor = jwt.getClaim("usuarioId");

        if (!(valor instanceof Number numero)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login novamente para atualizar sua sessão"
            );
        }

        return numero.intValue();
    }
}