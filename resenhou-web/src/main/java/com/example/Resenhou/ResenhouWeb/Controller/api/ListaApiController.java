package com.example.Resenhou.ResenhouWeb.Controller.api;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/listas")
public class ListaApiController {

    private final RestClient leitura = RestClient.builder()
            .baseUrl("http://leitura:8080")
            .build();

    public record ListaRequest(
            String nome,
            String descricao,
            boolean publica,
            boolean visualicacaoPerfil,
            List<Integer> livroIds
    ) {}

    private String obterToken(HttpSession session) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para continuar"
            );
        }

        return token;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String listar(HttpSession session) {
        return leitura.get()
                .uri("/api/listas")
                .headers(headers ->
                        headers.setBearerAuth(obterToken(session)))
                .retrieve()
                .body(String.class);
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String buscarPorId(
            HttpSession session,
            @PathVariable Integer id
    ) {
        return leitura.get()
                .uri("/api/listas/{id}", id)
                .headers(headers ->
                        headers.setBearerAuth(obterToken(session)))
                .retrieve()
                .body(String.class);
    }

    @GetMapping(
            value = "/buscar/nome",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String buscarPublicas(
            HttpSession session,
            @RequestParam String nome
    ) {
        return leitura.get()
                .uri(builder -> builder
                        .path("/api/listas/buscar/nome")
                        .queryParam("nome", "{nome}")
                        .build(nome))
                .headers(headers ->
                        headers.setBearerAuth(obterToken(session)))
                .retrieve()
                .body(String.class);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> salvar(
            HttpSession session,
            @RequestBody ListaRequest dados
    ) {
        String resposta = leitura.post()
                .uri("/api/listas")
                .headers(headers ->
                        headers.setBearerAuth(obterToken(session)))
                .contentType(MediaType.APPLICATION_JSON)
                .body(dados)
                .retrieve()
                .body(String.class);

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(resposta);
    }

    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public String atualizar(
            HttpSession session,
            @PathVariable Integer id,
            @RequestBody ListaRequest dados
    ) {
        return leitura.put()
                .uri("/api/listas/{id}", id)
                .headers(headers ->
                        headers.setBearerAuth(obterToken(session)))
                .contentType(MediaType.APPLICATION_JSON)
                .body(dados)
                .retrieve()
                .body(String.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(
            HttpSession session,
            @PathVariable Integer id
    ) {
        leitura.delete()
                .uri("/api/listas/{id}", id)
                .headers(headers ->
                        headers.setBearerAuth(obterToken(session)))
                .retrieve()
                .toBodilessEntity();

        return ResponseEntity.noContent().build();
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