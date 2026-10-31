package com.example.Resenhou.ResenhouWeb.Controller.api;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/autores")
public class AutorApiController {

    private final RestClient catalogo = RestClient.builder()
            .baseUrl("http://catalogo:8080")
            .build();

    @GetMapping
    public String listar(HttpSession session) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para consultar os autores"
            );
        }

        return catalogo.get()
                .uri("/api/autores")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .body(String.class);
    }
    public record AutorRequest(
        String nome,
        String nacionalidade,
        java.time.LocalDate dataNascimento,
        String biografia
    ) {}

    @PostMapping
    public String salvarAutor(HttpSession session, @RequestBody AutorRequest autor) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para cadastrar autores"
            );
        }

        return catalogo.post()
                .uri("/api/autores")
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(autor)
                .retrieve()
                .body(String.class);
    }

    @PutMapping("/{id}")
    public String atualizarAutor(HttpSession session, @PathVariable Integer id,@RequestBody AutorRequest autor) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para atualizar autores"
            );
        }

        return catalogo.put()
                .uri("/api/autores/{id}", id)
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(autor)
                .retrieve()
                .body(String.class);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirAutor(HttpSession session, @PathVariable Integer id) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para excluir autores"
            );
        }

        catalogo.delete()
                .uri("/api/autores/{id}", id)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .toBodilessEntity();

        return ResponseEntity.noContent().build();
    }
}