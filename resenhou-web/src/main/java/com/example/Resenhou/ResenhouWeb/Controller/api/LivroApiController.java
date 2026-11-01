package com.example.Resenhou.ResenhouWeb.Controller.api;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/livros")
public class LivroApiController {

    private final RestClient catalogo = RestClient.builder()
            .baseUrl("http://catalogo:8080")
            .build();

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String listar(HttpSession session) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para consultar os livros"
            );
        }

        return catalogo.get()
                .uri("/api/livros")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .body(String.class);
    }
    public record ReferenciaRequest(Integer id) {}
    public record LivroRequest(
            String nome,
            String titulo,
            String sinopse,
            String isbn,
            String tituloOriginal,
            Integer numeroPaginas,
            java.time.LocalDate dataPublicacao,
            ReferenciaRequest editora,
            java.util.List<ReferenciaRequest> autores,
            java.util.List<ReferenciaRequest> generos
    ) {}

    @PostMapping
    public String salvarLivro(HttpSession session, @RequestBody LivroRequest livro) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para cadastrar livros"
            );
        }

        return catalogo.post()
                .uri("/api/livros")
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(livro)
                .retrieve()
                .body(String.class);
    }

    @PutMapping("/{id}")
    public String atualizarLivro(HttpSession session, @PathVariable Integer id,@RequestBody LivroRequest livro) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para atualizar livros"
            );
        }

        return catalogo.put()
                .uri("/api/livros/{id}", id)
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(livro)
                .retrieve()
                .body(String.class);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirLivro(HttpSession session, @PathVariable Integer id) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para excluir livros"
            );
        }

        catalogo.delete()
                .uri("/api/livros/{id}", id)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .toBodilessEntity();

        return ResponseEntity.noContent().build();
    }
    
}