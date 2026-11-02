package com.example.Resenhou.Leitura.Service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

@Component
public class CatalogoClient {

    private final RestClient catalogo;

    public CatalogoClient(
            @Value("${services.catalogo.url:http://catalogo:8080}")
            String url
    ) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(5));
        factory.setReadTimeout(Duration.ofSeconds(10));

        catalogo = RestClient.builder()
                .baseUrl(url)
                .requestFactory(factory)
                .build();
    }

    public void validarLivro(Integer livroId) {
        var authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if (authentication == null ||
                !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Faça login para continuar"
            );
        }

        try {
            catalogo.get()
                    .uri("/api/livros/{id}", livroId)
                    .headers(headers ->
                            headers.setBearerAuth(jwt.getTokenValue()))
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientResponseException erro) {
            int status = erro.getStatusCode().value();

            if (status == 404) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Livro não encontrado: " + livroId
                );
            }

            if (status == 401 || status == 403) {
                throw new ResponseStatusException(
                        erro.getStatusCode(),
                        "Não foi possível autorizar a consulta ao catálogo"
                );
            }

            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY,
                    "O catálogo não conseguiu validar o livro"
            );

        } catch (ResourceAccessException erro) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Catálogo indisponível. Tente novamente."
            );
        }
    }
}