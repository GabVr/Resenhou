package com.example.Resenhou.ResenhouWeb.Controller.api;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/perfil")
public class PerfilApiController {

    private final RestClient usuarios;

    public PerfilApiController(
            @Value("${services.usuarios.url}") String url
    ) {
        usuarios = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    public record PerfilRequest(
            String nome,
            String biografia
    ) {}

    private String obterToken(HttpSession session) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para acessar seu perfil"
            );
        }

        return token;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String consultar(HttpSession session) {
        return usuarios.get()
                .uri("/api/usuarios/me")
                .headers(headers ->
                        headers.setBearerAuth(obterToken(session)))
                .retrieve()
                .body(String.class);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String atualizar(
            HttpSession session,
            @RequestBody PerfilRequest dados
    ) {
        return usuarios.put()
                .uri("/api/usuarios/me")
                .headers(headers ->
                        headers.setBearerAuth(obterToken(session)))
                .contentType(MediaType.APPLICATION_JSON)
                .body(dados)
                .retrieve()
                .body(String.class);
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<byte[]> tratarErro(
            RestClientResponseException erro
    ) {
        MediaType tipo = erro.getResponseHeaders() == null
                ? null
                : erro.getResponseHeaders().getContentType();

        return ResponseEntity.status(erro.getStatusCode())
                .contentType(tipo == null
                        ? MediaType.APPLICATION_JSON
                        : tipo)
                .body(erro.getResponseBodyAsByteArray());
    }
}