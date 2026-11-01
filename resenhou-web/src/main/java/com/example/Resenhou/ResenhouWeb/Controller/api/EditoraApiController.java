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
@RequestMapping("/api/editoras")
public class EditoraApiController {

    private final RestClient catalogo = RestClient.builder()
            .baseUrl("http://catalogo:8080")
            .build();

    @GetMapping
    public String listar(HttpSession session) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para consultar as editoras"
            );
        }

        return catalogo.get()
                .uri("/api/editoras")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .body(String.class);
    }
    public record EditoraRequest(
        String nome,
        String pais,
        String site
    ) {}

    @PostMapping
    public String salvarEditora(HttpSession session, @RequestBody EditoraRequest editora) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para cadastrar editoras"
            );
        }

        return catalogo.post()
                .uri("/api/editoras")
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(editora)
                .retrieve()
                .body(String.class);
    }

    @PutMapping("/{id}")
    public String atualizarEditora(HttpSession session, @PathVariable Integer id,@RequestBody EditoraRequest editora) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para atualizar editoras"
            );
        }

        return catalogo.put()
                .uri("/api/editoras/{id}", id)
                .headers(headers -> headers.setBearerAuth(token))
                .contentType(MediaType.APPLICATION_JSON)
                .body(editora)
                .retrieve()
                .body(String.class);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirEditora(HttpSession session, @PathVariable Integer id) {
        String token = (String) session.getAttribute("accessToken");

        if (token == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para excluir editoras"
            );
        }

        catalogo.delete()
                .uri("/api/editoras/{id}", id)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .toBodilessEntity();

        return ResponseEntity.noContent().build();
    }
}
    
