package com.example.Resenhou.ResenhouWeb.Controller.api;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/generos")
public class GeneroApiController {

    private final RestClient catalogo = RestClient.builder()
            .baseUrl("http://catalogo:8080")
            .build();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String listar(HttpSession session) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para consultar os gêneros"
            );
        }

        return catalogo.get()
                .uri("/api/generos")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .body(String.class);
    }
}